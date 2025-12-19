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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<OrderDTO> getOrdersBySellerId(int sellerId) {
        List<Order> orders = orderRepository.findBySeller_Id(sellerId);
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

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
            dto.setCustomerName(order.getCustomer().getFullName());
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
        order.setCustomerName(dto.getCustomerName());
        User customer = userRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        order.setCustomer(customer);
        User seller = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        order.setSeller(seller);
        order.setPhone(dto.getPhone());
        order.setOrderDate(dto.getOrderDate());
        order.setStatus(dto.getStatus());
        order.setTotalAmount(dto.getTotalAmount());
        order.setNote(dto.getNote());
        List<OrderItem> items = null;
        if (dto.getOrderItems() != null && dto.getOrderItems().size() > 0) {
            items = dto.getOrderItems().stream()
                    .map(itemDTO -> {
                        OrderItem item = new OrderItem();
                        item.setId(itemDTO.getId());
                        Product product = productRepository.findById(itemDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
                        item.setProduct(product);
                        item.setQuantity(itemDTO.getQuantity());
                        item.setUnitWeight(itemDTO.getUnitWeight());
                        item.setSubTotal(item.getSubTotal());
                        item.setOrder(order);
                        return item;
                    }).collect(Collectors.toList());
        }
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
        dto.setSubTotal(orderDetail.getQuantity() * orderDetail.getUnitWeight());
        return dto;
    }
}
