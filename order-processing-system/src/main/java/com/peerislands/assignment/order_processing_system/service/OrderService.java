package com.peerislands.assignment.order_processing_system.service;

import com.peerislands.assignment.order_processing_system.dto.*;
import com.peerislands.assignment.order_processing_system.entity.OrderStatus;
import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO request);
    OrderResponseDTO getOrder(Long id);
    List<OrderResponseDTO> listOrders(OrderStatus status);
    void cancelOrder(Long id);
    void updatePendingOrders();
}
