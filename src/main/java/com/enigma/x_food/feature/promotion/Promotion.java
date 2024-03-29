package com.enigma.x_food.feature.promotion;
import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.promotion_status.PromotionStatus;
import com.enigma.x_food.shared.BaseEntity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "Promotion")
public class Promotion extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "promotion_id")
    private String promotionID;
    @ManyToOne
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;
    @Column(name = "cost", nullable = false, length = 11)
    private Integer cost;
    @Column(name = "max_redeem", nullable = false, length = 11)
    private Integer maxRedeem;
    @Column(name = "promotion_value", nullable = false, length = 11)
    private Integer promotionValue;
    @Column(name = "promotion_description", nullable = false, length = 150)
    private String promotionDescription;
    @Column(name = "promotion_name", nullable = false, length = 100)
    private String promotionName;
    @Column(name = "quantity", nullable = false, length = 11)
    private Integer quantity;
    @Column(name = "expired_date", nullable = false)
    private Timestamp expiredDate;
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
    @ManyToOne
    @JoinColumn(name = "promotion_status_id", nullable = false)
    private PromotionStatus promotionStatus;
    @Column(name = "notes", length = 150)
    private String notes;
}
