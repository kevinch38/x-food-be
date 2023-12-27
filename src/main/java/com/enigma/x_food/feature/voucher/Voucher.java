package com.enigma.x_food.feature.voucher;

import com.enigma.x_food.feature.balance.Balance;
import com.enigma.x_food.feature.history.History;
import com.enigma.x_food.feature.loyalty_point.LoyaltyPoint;
import com.enigma.x_food.feature.voucher_status.VoucherStatus;
import com.enigma.x_food.feature.otp.OTP;
import com.enigma.x_food.feature.pin.Pin;
import com.enigma.x_food.feature.promotion.Promotion;
import com.enigma.x_food.feature.promotion_status.PromotionStatus;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "Voucher")
public class Voucher extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "voucher_id")
    private String voucherID;

    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private User user;

    @Column(name = "voucher_value", nullable = false, length = 11)
    private Integer voucherValue;

    @Column(name="expired_date",nullable = false)
    private Timestamp expiredDate;

    @Column(name="voucher_code",nullable = false, unique = true)
    private String voucherCode;

    @ManyToOne
    @JoinColumn(name = "voucher_status_id", nullable = false)
    private VoucherStatus voucherStatus;
}
