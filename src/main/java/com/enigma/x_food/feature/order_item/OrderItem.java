package com.enigma.x_food.feature.order_item;

import com.enigma.x_food.feature.item.Item;
import com.enigma.x_food.feature.order.Order;
import com.enigma.x_food.feature.sub_variety.SubVariety;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "order_item")
public class OrderItem extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "order_item_id")
    private String orderItemID;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name = "sub_variety_id", nullable = false)
    private SubVariety subVariety;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
