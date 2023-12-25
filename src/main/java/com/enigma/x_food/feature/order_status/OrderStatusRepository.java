package com.enigma.x_food.feature.order_status;

import com.enigma.x_food.constant.EOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, String> {
    Optional<OrderStatus> findByStatus(EOrderStatus status);
}
