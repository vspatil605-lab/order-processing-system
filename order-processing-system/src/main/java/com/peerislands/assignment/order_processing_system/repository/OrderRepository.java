package com.peerislands.assignment.order_processing_system.repository;

import com.peerislands.assignment.order_processing_system.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus status);
}
