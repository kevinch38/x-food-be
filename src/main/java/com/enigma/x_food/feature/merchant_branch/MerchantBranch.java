package com.enigma.x_food.feature.merchant_branch;

import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.branch_working_hours.BranchWorkingHours;
import com.enigma.x_food.feature.city.City;
import com.enigma.x_food.feature.item.Item;
import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant_branch_status.MerchantBranchStatus;
import com.enigma.x_food.feature.sub_variety.SubVariety;
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
    @Column(name = "pic_name", nullable = false, length = 50)
    private String picName;
    @Column(name = "pic_number", nullable = false, length = 15)
    private String picNumber;
    @Column(name = "pic_email", nullable = false, length = 100)
    private String picEmail;
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;
    @Column(name = "join_date", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Timestamp joinDate;
    @Lob
    @Column(name = "image")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
    @OneToMany(mappedBy = "merchantBranch")
    private List<Item> items;
    @ManyToOne
    @JoinColumn(name = "merchant_branch_status_id", nullable = false)
    private MerchantBranchStatus merchantBranchStatus;
    @OneToMany(mappedBy = "merchantBranch")
    List<SubVariety> subVarieties;
    @OneToMany(mappedBy = "merchantBranch")
    List<BranchWorkingHours> branchWorkingHours;
}
