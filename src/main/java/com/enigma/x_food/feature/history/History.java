package com.enigma.x_food.feature.history;

import com.enigma.x_food.feature.order.Order;
import com.enigma.x_food.feature.payment.Payment;
import com.enigma.x_food.feature.top_up.TopUp;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.shared.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "history")
public class History extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "history_id")
    private String historyID;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "history_value")
    private Double historyValue;
    @Column(name = "transaction_date")
    private LocalDate transactionDate;
    @Column(name = "credit")
    private Boolean credit;
    @Column(name = "debit")
    private Boolean debit;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
    @OneToOne
    @JoinColumn(name = "top_up_id")
    private TopUp topUp;
    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private User user;
}
