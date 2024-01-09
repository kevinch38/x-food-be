package com.enigma.x_food.feature.promotion_update_request;

import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.promotion_status.PromotionStatus;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "promotion_update_request")
public class PromotionUpdateRequest extends BaseEntity {
    @Id
    @Column(name = "promotion_id")
    private String promotionID;
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;
    @Column(name = "cost")
    private Integer cost;
    @Column(name = "max_redeem")
    private Integer maxRedeem;
    @Column(name = "promotion_value")
    private Integer promotionValue;
    @Column(name = "promotion_description")
    private String promotionDescription;
    @Column(name = "promotion_name")
    private String promotionName;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "expired_date")
    private Timestamp expiredDate;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
    @ManyToOne
    @JoinColumn(name = "promotion_status_id")
    private PromotionStatus promotionStatus;
    @Column(name = "notes")
    private String notes;
}
