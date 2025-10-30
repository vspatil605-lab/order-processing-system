package com.peerislands.assignment.order_processing_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private String productName;
    private Integer quantity;
    private Double price;
}
