package com.enigma.x_food.feature.otp;

import com.enigma.x_food.shared.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "OTP")
public class OTP extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    private String otpID;

    @Column(name = "otp", nullable = false, length = 64)
    private String otp;

    @Column(name = "account_id", nullable = false, unique = true, length = 36)
    private String accountID;
}
