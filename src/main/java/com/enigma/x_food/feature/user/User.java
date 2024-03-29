package com.enigma.x_food.feature.user;

import com.enigma.x_food.feature.balance.Balance;
import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.loyalty_point.LoyaltyPoint;
import com.enigma.x_food.feature.otp.OTP;
import com.enigma.x_food.feature.pin.Pin;
import com.enigma.x_food.feature.role.Role;
import com.enigma.x_food.feature.voucher.Voucher;
import com.enigma.x_food.shared.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "account")
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pin_id")
    @JsonBackReference
    private Pin pin;

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Lob
    @Column(name = "profile_photo", nullable = false)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] profilePhoto;

    @OneToOne
    @JoinColumn(name = "balance_id", nullable = false)
    @JsonBackReference
    private Balance balance;

    @OneToOne
    @JoinColumn(name = "loyalty_point_id", nullable = false)
    @JsonBackReference
    private LoyaltyPoint loyaltyPoint;

    @JoinColumn(name = "otp_id")
    @OneToOne
    @JsonBackReference
    private OTP otp;

    @JoinColumn(name = "role_id")
    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<History> histories;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<Voucher> vouchers;
}
