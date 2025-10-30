package com.peerislands.assignment.order_processing_system.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {
    private String customerName;
    private List<OrderItemDTO> items;
}
