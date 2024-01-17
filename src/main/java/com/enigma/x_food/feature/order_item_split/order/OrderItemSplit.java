package com.enigma.x_food.feature.order_item_split.order;

import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.order_item.OrderItem;
import com.enigma.x_food.feature.order_status.OrderStatus;
import com.enigma.x_food.feature.payment.Payment;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "order_item_split")
public class OrderItemSplit extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "order_item_split_id")
    private String orderItemSplitID;
    @Column(name = "order_item_split_item")
    private String orderItemID;
    @ManyToOne
    private Payment payment;
}
