package com.pdl.bookingmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private int id;
    private int orderId;
    private double amount;
    private String method;
    private boolean status;
    private LocalDateTime paymentDate;
}
