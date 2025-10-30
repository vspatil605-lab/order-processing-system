package com.peerislands.assignment.order_processing_system.config;

import com.peerislands.assignment.order_processing_system.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class SchedulerConfig {

    private final OrderService orderService;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void processPendingOrders() {
        orderService.updatePendingOrders();
    }
}
