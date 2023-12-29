package com.enigma.x_food.feature.admin;

import com.enigma.x_food.feature.balance.Balance;
import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.loyalty_point.LoyaltyPoint;
import com.enigma.x_food.feature.otp.OTP;
import com.enigma.x_food.feature.pin.Pin;
import com.enigma.x_food.feature.role.Role;
import com.enigma.x_food.feature.voucher.Voucher;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "admin")
public class Admin extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "admin_id")
    private String adminID;

    @Column(name = "ktp_id", nullable = false, length = 64)
    private String adminName;

    @Column(name = "admin_email", unique = true, nullable = false, length = 100)
    private String adminEmail;

    @Column(name = "phone_number", nullable = false, length = 15, unique = true)
    private Boolean isSuperAdmin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pin_id")
    private Role pin;

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
    private Balance balance;

    @OneToOne
    @JoinColumn(name = "loyalty_point_id", nullable = false)
    private LoyaltyPoint loyaltyPoint;

    @JoinColumn(name = "otp_id")
    @OneToOne
    private OTP otp;

    @OneToMany(mappedBy = "user")
    private List<History> histories;

    @OneToMany(mappedBy = "user")
    private List<Voucher> vouchers;
}
