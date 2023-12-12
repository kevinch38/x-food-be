package com.enigma.x_food.feature.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "Account")
public class User {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    private String accountID;

    @Column(name = "ktp_id", nullable = false, length = 64)
    private String ktpID;

    @Column(name = "account_email", nullable = false, length = 100)
    private String accountEmail;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "pin_id", nullable = false)
    private String pinID;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "balance_id", nullable = false)
    private String balanceID;

    @Column(name = "loyalty_point_id", nullable = false)
    private String loyaltyPointID;

    @Column(name = "otp_id", nullable = false)
    private String otpID;
}
