package com.enigma.x_food.feature.order_status;

import com.enigma.x_food.constant.EOrderStatus;
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
@Table(name = "order_status")
public class OrderStatus extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "order_status_id")
    private String orderStatusID;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 100)
    private EOrderStatus status;
}
