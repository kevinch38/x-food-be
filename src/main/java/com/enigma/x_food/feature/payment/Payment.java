package com.enigma.x_food.feature.payment;

import com.enigma.x_food.feature.friend.Friend;
import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.order.Order;
import com.enigma.x_food.feature.order_item_split.order.OrderItemSplit;
import com.enigma.x_food.feature.order_item_split.order.dto.response.OrderItemSplitResponse;
import com.enigma.x_food.feature.payment_status.PaymentStatus;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "payment_id")
    private String paymentID;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private User user;
    @Column(name = "payment_amount", nullable = false, length = 11)
    private Double paymentAmount;
    @Column(name = "payment_type", nullable = false)
    private String paymentType;
    @Column(name = "expired_at", nullable = false)
    private Timestamp expiredAt;
    @ManyToOne
    @JoinColumn(name = "payment_status_id", nullable = false)
    private PaymentStatus paymentStatus;
    @ManyToOne
    @JoinColumn(name = "history_id")
    private History history;
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private Friend friend;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @OneToMany(mappedBy = "payment")
    private List<OrderItemSplit> orderItemSplits;
}
