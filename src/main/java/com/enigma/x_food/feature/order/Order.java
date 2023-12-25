package com.enigma.x_food.feature.order;

import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.order_status.OrderStatus;
import com.enigma.x_food.feature.payment.Payment;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "\"order\"")
public class Order extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "order_id")
    private String orderID;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private User user;
    @OneToOne
    @JoinColumn(name = "history_id", nullable = false)
    private History history;
    @Column(name = "order_value", nullable = false, length = 11)
    private Double orderValue;
    @Column(name = "notes", length = 150)
    private String notes;
    @Column(name = "table_number", nullable = false, length = 11)
    private Integer tableNumber;
    @ManyToOne
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatus orderStatus;
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private MerchantBranch merchantBranch;
    @OneToMany(mappedBy = "order")
    private List<Payment> payments;
}
