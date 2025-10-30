package com.peerislands.assignment.order_processing_system.service;

import com.peerislands.assignment.order_processing_system.dto.*;
import com.peerislands.assignment.order_processing_system.entity.*;
import com.peerislands.assignment.order_processing_system.event.OrderEventProducer;
import com.peerislands.assignment.order_processing_system.exception.*;
import com.peerislands.assignment.order_processing_system.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer eventProducer;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .status(OrderStatus.PENDING)
                .items(request.getItems().stream()
                        .map(i -> OrderItem.builder()
                                .productName(i.getProductName())
                                .quantity(i.getQuantity())
                                .price(i.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        order.getItems().forEach(i -> i.setOrder(order));
        Order saved = orderRepository.save(order);
        eventProducer.publishOrderEvent(saved);

        return mapToResponse(saved);
    }

    @Override
    public OrderResponseDTO getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return mapToResponse(order);
    }

    @Override
    public List<OrderResponseDTO> listOrders(OrderStatus status) {
        List<Order> orders = (status == null)
                ? orderRepository.findAll()
                : orderRepository.findByStatus(status);
        return orders.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() != OrderStatus.PENDING)
            throw new InvalidOrderStateException("Only PENDING orders can be cancelled");

        order.setStatus(OrderStatus.CANCELLED);
        eventProducer.publishOrderEvent(order);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void updatePendingOrders() {
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        pendingOrders.forEach(order -> {
            order.setStatus(OrderStatus.PROCESSING);
            eventProducer.publishOrderEvent(order);
        });
        orderRepository.saveAll(pendingOrders);
    }

    private OrderResponseDTO mapToResponse(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .status(order.getStatus())
                .items(order.getItems().stream()
                        .map(i -> new OrderItemDTO(i.getProductName(), i.getQuantity(), i.getPrice()))
                        .collect(Collectors.toList()))
                .build();
    }
}
