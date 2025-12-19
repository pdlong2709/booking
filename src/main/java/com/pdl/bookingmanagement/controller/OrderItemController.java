package com.pdl.bookingmanagement.controller;

import com.pdl.bookingmanagement.dto.OrderItemDTO;
import com.pdl.bookingmanagement.model.OrderItem;
import com.pdl.bookingmanagement.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderItems")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping("/findByOrder")
    public ResponseEntity<?> findByOrder(@RequestParam Integer orderId) {
        List<OrderItemDTO> orderItems = orderItemService.findByOrderId(orderId);
        return ResponseEntity.ok().body(orderItems);
    }
}
