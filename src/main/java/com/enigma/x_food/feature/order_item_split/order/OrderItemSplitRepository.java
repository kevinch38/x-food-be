package com.enigma.x_food.feature.order_item_split.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemSplitRepository extends JpaRepository<OrderItemSplit, String>, JpaSpecificationExecutor<OrderItemSplit> {
}
