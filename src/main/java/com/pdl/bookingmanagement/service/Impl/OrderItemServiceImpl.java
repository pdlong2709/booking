package com.pdl.bookingmanagement.service.Impl;

import com.pdl.bookingmanagement.dto.OrderItemDTO;
import com.pdl.bookingmanagement.model.OrderItem;
import com.pdl.bookingmanagement.repository.OrderItemRepository;
import com.pdl.bookingmanagement.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    @Override
    public List<OrderItemDTO> findByOrderId(int orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder_Id(orderId);
        return orderItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private OrderItemDTO convertToDTO(OrderItem orderItem) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(orderItem.getId());
        dto.setOrderId(orderItem.getOrder().getId());
        dto.setProductId(orderItem.getProduct().getId());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitWeight(orderItem.getUnitWeight());
        dto.setSubTotal(orderItem.getQuantity() * orderItem.getUnitWeight());
        return dto;
    }
}
