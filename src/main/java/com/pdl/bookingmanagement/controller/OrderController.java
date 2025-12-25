package com.pdl.bookingmanagement.controller;

import com.pdl.bookingmanagement.dto.OrderDTO;
import com.pdl.bookingmanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/seller")
    public ResponseEntity<List<OrderDTO>> getOrdersBySeller(@RequestParam("id") int selletId) {
        List<OrderDTO> orders = orderService.getOrdersBySellerId(selletId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/searchOrder")
    public ResponseEntity<List<OrderDTO>> searchOrderByCustomerName(@RequestParam("name") String name, @RequestParam("sellerId") int sellerId) {
        List<OrderDTO> orders = orderService.findOrderByCustomerName(name, sellerId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/createOrder")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO newOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.ok(newOrder);
    }

    @PutMapping("/updateOrder")
    public ResponseEntity<OrderDTO> updateOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO updateOrder = orderService.updateOrder(orderDTO);
        return ResponseEntity.ok(updateOrder);
    }

    @PutMapping("/updateStatus")
    public ResponseEntity<OrderDTO> updateStatus(@RequestParam("id") int orderId) {
        OrderDTO updateStatus = orderService.updateStatus(orderId);
        return ResponseEntity.ok(updateStatus);
    }

    @PutMapping("/updatePaymentStatus")
    public ResponseEntity<OrderDTO> updatePaymentStatus(@RequestParam("id") int orderId) {
        OrderDTO updateStatus = orderService.updatePaymentStatus(orderId);
        return ResponseEntity.ok(updateStatus);
    }
}
