package com.pdl.bookingmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Integer id;
    private Integer customerId;
    private String customerName;
    private String phone;
    private Integer sellerId;
    private String sellerName;
    private LocalDateTime orderDate;
    private String status;
    private double totalAmount;
    private String note;
    private List<OrderItemDTO> orderItems;
}
