package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.promotion.Promotion;
import com.enigma.x_food.shared.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "merchant")
public class Merchant extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "merchant_id")
    private String merchantID;
    @Column(name = "join_date", nullable = false)
    private Timestamp joinDate;
    @Column(name = "merchant_name", nullable = false, length = 50)
    private String merchantName;
    @Column(name = "pic_name", nullable = false, length = 50)
    private String picName;
    @Column(name = "pic_number", nullable = false, length = 15)
    private String picNumber;
    @Column(name = "pic_email", nullable = false, length = 100)
    private String picEmail;
    @Column(name = "merchant_description", nullable = false, length = 150)
    private String merchantDescription;
    @Column(name = "admin_id", nullable = false, length = 36)
    private String adminID;
    @Column(name = "merchant_status_id", nullable = false, length = 36)
    private String merchantStatusID;
    @Column(name = "notes", length = 150)
    private String notes;
    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    private List<MerchantBranch> merchantBranches;
    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    private List<Promotion> promotions;
}
