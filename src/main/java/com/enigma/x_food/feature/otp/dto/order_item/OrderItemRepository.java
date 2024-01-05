package com.enigma.x_food.feature.otp.dto.order_item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String>, JpaSpecificationExecutor<OrderItem> {
}
