package com.enigma.x_food.feature.payment_status;

import com.enigma.x_food.constant.EPaymentStatus;
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
@Table(name = "payment_status")
public class PaymentStatus extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "payment_status_id")
    private String paymentStatusID;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 100)
    private EPaymentStatus status;
}
