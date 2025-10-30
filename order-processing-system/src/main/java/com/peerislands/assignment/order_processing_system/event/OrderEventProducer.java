package com.peerislands.assignment.order_processing_system.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peerislands.assignment.order_processing_system.dto.OrderItemDTO;
import com.peerislands.assignment.order_processing_system.dto.OrderResponseDTO;
import com.peerislands.assignment.order_processing_system.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishOrderEvent(Order order) {
        try {
            // 🔹 Convert entity -> DTO to avoid circular reference
            OrderResponseDTO event = OrderResponseDTO.builder()
                    .id(order.getId())
                    .customerName(order.getCustomerName())
                    .status(order.getStatus())
                    .items(order.getItems().stream()
                            .map(i -> new OrderItemDTO(i.getProductName(), i.getQuantity(), i.getPrice()))
                            .collect(Collectors.toList()))
                    .build();

            // 🔹 Serialize clean DTO to JSON
            String orderJson = objectMapper.writeValueAsString(event);

            // 🔹 Send serialized JSON to Kafka
            kafkaTemplate.send("order-events", orderJson);

            System.out.println("✅ Order event sent to Kafka: " + orderJson);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to serialize order for Kafka", e);
        }
    }
}
