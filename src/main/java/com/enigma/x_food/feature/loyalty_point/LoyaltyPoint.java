package com.enigma.x_food.feature.loyalty_point;

import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "loyalty_point")
public class LoyaltyPoint extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "loyalty_point_id")
    private String loyaltyPointID;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "loyalty_point_amount", nullable = false, length = 11)
    private Integer loyaltyPointAmount;
    @Column(name = "expired_date", nullable = false)
    private Timestamp expiredDate;
}
