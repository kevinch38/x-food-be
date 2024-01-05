package com.enigma.x_food.feature.order_item_sub_variety;

import com.enigma.x_food.feature.otp.dto.order_item.OrderItem;
import com.enigma.x_food.feature.sub_variety.SubVariety;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "order_item_sub_variety")
public class OrderItemSubVariety {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "order_item_sub_variety_id")
    private String orderItemSubVarietyID;
    @ManyToOne
    @JoinColumn(name = "sub_variety_id", nullable = false)
    private SubVariety subVariety;
    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;
}
