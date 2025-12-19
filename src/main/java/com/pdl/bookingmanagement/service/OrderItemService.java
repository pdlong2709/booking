package com.pdl.bookingmanagement.service;

import com.pdl.bookingmanagement.dto.OrderItemDTO;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDTO> findByOrderId(int orderId);
}
