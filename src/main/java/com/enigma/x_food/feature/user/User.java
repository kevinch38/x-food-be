package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.otp.OTP;
import com.enigma.x_food.feature.pin.Pin;
import com.enigma.x_food.shared.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "Account")
public class User extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "account_id")
    private String accountID;

    @Column(name = "ktp_id", nullable = false, length = 64)
    private String ktpID;

    @Column(name = "account_email", unique = true, nullable = false, length = 100)
    private String accountEmail;

    @Column(name = "phone_number", nullable = false, length = 15, unique = true)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "pin_id", unique = true)
    private Pin pin;

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "balance_id", nullable = false)
    private String balanceID;

    @Column(name = "loyalty_point_id", nullable = false)
    private String loyaltyPointID;

    @JoinColumn(name = "otp_id", unique = true)
    @OneToOne
    private OTP otp;

    @OneToMany(mappedBy = "user")
    private List<History> histories;
}
