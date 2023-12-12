package com.enigma.x_food.feature.merchant_branch.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@Entity
@Table(name = "Merchant_Branch")
public class MerchantBranch {
    @Id
    private String branchID;
    @Column(name = "merchant_id", nullable = false, length = 64)
    private String merchantID;
    @Column(name = "merchant_name", nullable = false, length = 64)
    private String branchName;
    @Column(name = "address", nullable = false, length = 150)
    private String address;
    @Column(name = "timezone", nullable = false, length = 50)
    private String timezone;
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
    @Column(name = "branch_working_hours_id", nullable = false)
    private String branchWorkingHoursID;
    @Column(name = "city_id", nullable = false)
    private String cityID;
}
