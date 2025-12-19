package com.pdl.bookingmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Integer id;
    private Integer orderId;
    private Integer productId;
    private String productName;
    private int quantity;
    private float unitWeight;
    private float subTotal;
}
