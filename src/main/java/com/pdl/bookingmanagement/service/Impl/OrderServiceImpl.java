package com.pdl.bookingmanagement.service.Impl;

import com.pdl.bookingmanagement.dto.OrderDTO;
import com.pdl.bookingmanagement.dto.OrderItemDTO;
import com.pdl.bookingmanagement.model.Order;
import com.pdl.bookingmanagement.model.OrderItem;
import com.pdl.bookingmanagement.model.Product;
import com.pdl.bookingmanagement.model.User;
import com.pdl.bookingmanagement.repository.OrderRepository;
import com.pdl.bookingmanagement.repository.ProductRepository;
import com.pdl.bookingmanagement.repository.UserRepository;
import com.pdl.bookingmanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<OrderDTO> getOrdersBySellerId(int sellerId) {
        List<Order> orders = orderRepository.findBySeller_IdOrderByOrderDateDesc(sellerId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findOrderByCustomerName(String name, int sellerId) {
        List<Order> orders = orderRepository.findByCustomerName(name, sellerId);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = orderRepository.save(convertToEntity(orderDTO));
        return convertToDTO(order);
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new RuntimeException("Order not found!"));
        if(order.getCustomer() != null){
            User customer = userRepository.findUserById(orderDTO.getCustomerId());
            order.setCustomer(customer);
            order.setCustomerName(orderDTO.getCustomerName());
            order.setPhone(orderDTO.getPhone());
        }
        User seller =  userRepository.findUserById(orderDTO.getSellerId());
        order.setSeller(seller);
        order.setStatus(orderDTO.getStatus());
        order.setNote(orderDTO.getNote());
        if(orderDTO.getOrderItems() != null){
            Map<Integer, OrderItem> existingItems = order.getOrderItems()
                    .stream()
                    .collect(Collectors.toMap(OrderItem::getId, i -> i));

            List<OrderItem> updatedItems = orderDTO.getOrderItems().stream()
                    .map(itemDTO -> {
                        OrderItem item;

                        // Nếu có ID → UPDATE
                        if (itemDTO.getId() != null && existingItems.containsKey(itemDTO.getId())) {
                            item = existingItems.get(itemDTO.getId());
                        }
                        // Nếu không có ID → CREATE
                        else {
                            item = new OrderItem();
                            item.setOrder(order);
                        }

                        Product product = productRepository.findById(itemDTO.getProductId())
                                .orElseThrow(() -> new RuntimeException("Product not found"));

                        item.setProduct(product);
                        item.setQuantity(itemDTO.getQuantity());
                        item.setUnitWeight(itemDTO.getUnitWeight());
                        item.setSubTotal(itemDTO.getSubTotal());
                        return item;
                    }).collect(Collectors.toList());

            // Gán lại list → orphanRemoval sẽ xóa item bị remove
            order.getOrderItems().clear();
            order.getOrderItems().addAll(updatedItems);
        }
        double totalAmount = order.getOrderItems()
                .stream()
                .mapToDouble(item ->
                        item.getQuantity()
                                * item.getUnitWeight()
                                * item.getSubTotal()
                )
                .sum();
        order.setTotalAmount(totalAmount);
        order.setTotalAmount(totalAmount);
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
            dto.setCustomerName(order.getCustomer().getFullName());
            dto.setPhone(order.getCustomer().getPhone());
        }
        if(order.getSeller() != null) {
            dto.setSellerId(order.getSeller().getId());
            dto.setSellerName(order.getSeller().getFullName());
        }
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        dto.setNote(order.getNote());

        if (order.getOrderItems() != null) {
            List<OrderItemDTO> detailDTOs = order.getOrderItems().stream()
                    .map(this::convertOrderItemToDTO)
                    .collect(Collectors.toList());
            dto.setOrderItems(detailDTOs);
        }

        return dto;
    }

    private Order convertToEntity(OrderDTO dto) {
        Order order = new Order();
        order.setId(dto.getId());
        User customer;
        if (dto.getCustomerId() != null && dto.getCustomerId() > 0) {
            customer = userRepository.findUserById(dto.getCustomerId());
            if (customer == null) {
                throw new RuntimeException("Customer not found");
            }
        } else {
            customer = new User();
            customer.setFullName(dto.getCustomerName());
            customer.setPhone(dto.getPhone());
            customer.setEmail(null);
            customer.setAddress(null);
            customer.setPassword("123456");
            customer.setRole("Customer");
            customer.setStatus(true);
            customer = userRepository.save(customer);
        }
        order.setCustomer(customer);
        order.setCustomerName(customer.getFullName());
        // ===== SELLER =====
        User seller = userRepository.findUserById(dto.getSellerId());
        order.setSeller(seller);
        order.setPhone(dto.getPhone());
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(dto.getStatus());
        order.setNote(dto.getNote());
        // ===== ORDER ITEMS =====
        List<OrderItem> items = null;
        if (dto.getOrderItems() != null && !dto.getOrderItems().isEmpty()) {
            items = dto.getOrderItems().stream().map(itemDTO -> {
                OrderItem item = new OrderItem();
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                item.setProduct(product);
                item.setQuantity(itemDTO.getQuantity());
                item.setUnitWeight(itemDTO.getUnitWeight());
                item.setSubTotal(itemDTO.getSubTotal());
                item.setOrder(order);
                return item;
            }).collect(Collectors.toList());
        }
        double totalAmount = order.getOrderItems()
                .stream()
                .mapToDouble(item ->
                        item.getQuantity()
                                * item.getUnitWeight()
                                * item.getSubTotal()
                )
                .sum();

        order.setTotalAmount(totalAmount);
        order.setOrderItems(items);
        return order;
    }

    private OrderItemDTO convertOrderItemToDTO(OrderItem orderDetail) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderDetail.getId());
        dto.setOrderId(orderDetail.getOrder().getId());

        if (orderDetail.getProduct() != null) {
            dto.setProductId(orderDetail.getProduct().getId());
            dto.setProductName(orderDetail.getProduct().getName());
        }

        dto.setQuantity(orderDetail.getQuantity());
        dto.setUnitWeight(orderDetail.getUnitWeight());
        dto.setSubTotal(orderDetail.getSubTotal());
        return dto;
    }
}
