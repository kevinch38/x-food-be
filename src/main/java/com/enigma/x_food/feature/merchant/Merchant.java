package com.enigma.x_food.feature.merchant;

import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.merchant_status.MerchantStatus;
import com.enigma.x_food.feature.promotion.Promotion;
import com.enigma.x_food.shared.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

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
    @Column(name = "join_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
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
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
    @ManyToOne
    @JoinColumn(name = "merchant_status_id", nullable = false)
    private MerchantStatus merchantStatus;
    @Column(name = "notes", length = 150)
    private String notes;
    @Lob
    @Column(name = "image", nullable = false)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;
    @Lob
    @Column(name = "logo_image", nullable = false)
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] logoImage;
    @OneToMany(mappedBy = "merchant")
    private List<MerchantBranch> merchantBranches;
    @OneToMany(mappedBy = "merchant")
    private List<Promotion> promotions;
}
