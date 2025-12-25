package com.pdl.bookingmanagement.service;

import com.pdl.bookingmanagement.dto.OrderDTO;

import java.util.List;

public interface OrderService{
    List<OrderDTO> getOrdersBySellerId(int sellerId);
    List<OrderDTO> findOrderByCustomerName(String name, int sellerId);
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO updateOrder(OrderDTO orderDTO);
    OrderDTO updateStatus(int orderId);
    OrderDTO updatePaymentStatus(int orderId);
}
