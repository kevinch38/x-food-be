package com.enigma.x_food.feature.promotion_status;

import com.enigma.x_food.constant.EPromotionStatus;
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
@Table(name = "promotion_status")
public class PromotionStatus extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "promotion_status_id")
    private String promotionStatusID;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 100)
    private EPromotionStatus status;
}
