package com.peerislands.assignment.order_processing_system.dto;

import com.peerislands.assignment.order_processing_system.entity.OrderStatus;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private String customerName;
    private OrderStatus status;
    private List<OrderItemDTO> items;
}
