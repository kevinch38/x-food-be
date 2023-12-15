package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant.Merchant;
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
@Table(name = "merchant_branch")
public class MerchantBranch extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "branch_id")
    private String branchID;
    @JoinColumn(name = "merchant_id", nullable = false)
    @ManyToOne
    private Merchant merchant;
    @Column(name = "merchant_name", nullable = false, length = 100)
    private String branchName;
    @Column(name = "address", nullable = false, length = 150)
    private String address;
    @Column(name = "timezone", nullable = false, length = 50)
    private String timezone;
    @Column(name = "branch_working_hours_id", nullable = false)
    private String branchWorkingHoursID;
    @Column(name = "city_id", nullable = false, length = 36)
    private String cityID;
}
