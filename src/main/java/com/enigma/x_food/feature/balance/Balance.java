package com.enigma.x_food.feature.balance;

import com.enigma.x_food.feature.user.User;
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
@Table(name = "balance")
public class Balance extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "balance_id")
    private String balanceID;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "total_balance", nullable = false, length = 11)
    private Double totalBalance;
}
