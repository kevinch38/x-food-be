package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "Merchant_Branch")
public class MerchantBranch extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
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
