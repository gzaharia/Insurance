package com.internship.insurance.repository;

import com.internship.insurance.model.Order;
import com.internship.insurance.model.OrderStatus;
import com.internship.insurance.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(OrderStatus status);
}
