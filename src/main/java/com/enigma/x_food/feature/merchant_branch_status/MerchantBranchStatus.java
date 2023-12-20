package com.enigma.x_food.feature.merchant_branch_status;

import com.enigma.x_food.constant.EMerchantBranchStatus;
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
@Table(name = "merchant_branch_status")
public class MerchantBranchStatus extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "merchant_branch_status_id")
    private String merchantBranchStatusID;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 100)
    private EMerchantBranchStatus status;
}
