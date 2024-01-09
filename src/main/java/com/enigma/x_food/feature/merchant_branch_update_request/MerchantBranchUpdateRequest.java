package com.enigma.x_food.feature.merchant_branch_update_request;

import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.feature.city.City;
import com.enigma.x_food.feature.item.Item;
import com.enigma.x_food.feature.merchant.Merchant;
import com.enigma.x_food.feature.merchant_branch_status.MerchantBranchStatus;
import com.enigma.x_food.feature.sub_variety.SubVariety;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "merchant_branch_update_request")
public class MerchantBranchUpdateRequest extends BaseEntity {
    @Id
    @Column(name = "branch_id")
    private String branchID;
    @JoinColumn(name = "merchant_id")
    @ManyToOne
    private Merchant merchant;
    @Column(name = "merchant_name")
    private String branchName;
    @Column(name = "address")
    private String address;
    @Column(name = "timezone")
    private String timezone;
    @Column(name = "branch_working_hours_id")
    private String branchWorkingHoursID;
    @Column(name = "pic_name")
    private String picName;
    @Column(name = "pic_number")
    private String picNumber;
    @Column(name = "pic_email")
    private String picEmail;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
    @Column(name = "join_date")
    private Timestamp joinDate;
    @Lob
    @Column(name = "image")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;
    @OneToMany(mappedBy = "merchantBranch")
    private List<Item> items;
    @ManyToOne
    @JoinColumn(name = "merchant_branch_status_id")
    private MerchantBranchStatus merchantBranchStatus;
    @OneToMany(mappedBy = "merchantBranch")
    List<SubVariety> subVarieties;
}
