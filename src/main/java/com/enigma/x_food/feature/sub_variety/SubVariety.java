package com.enigma.x_food.feature.sub_variety;

import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.user.variety_sub_variety.VarietySubVariety;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "sub_variety")
public class SubVariety {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "sub_variety_id")
    private String subVarietyID;

    @ManyToOne
    @JoinColumn(name = "merchant_branch_id", nullable = false)
    private MerchantBranch merchantBranch;

    @Column(name = "sub_var_name", nullable = false, length = 100)
    private String subVarName;

    @Column(name = "sub_var_stock", nullable = false, length = 11)
    private Integer subVarStock;

    @Column(name = "sub_var_price", nullable = false, length = 11)
    private Double subVarPrice;

    @OneToMany(mappedBy = "subVariety")
    private List<VarietySubVariety> varietySubVarieties;
}
