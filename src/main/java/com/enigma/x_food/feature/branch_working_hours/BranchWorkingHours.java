package com.enigma.x_food.feature.branch_working_hours;

import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import com.enigma.x_food.feature.user.User;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "branch_working_hours")
public class BranchWorkingHours extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "branch_working_hours_id")
    private String branchWorkingHoursID;
    @Column(name = "open_hour")
    private Timestamp openHour;
    @Column(name = "close_hour")
    private Timestamp closeHour;
    @Column(name = "days")
    private Timestamp days;
    @OneToOne
    @JoinColumn(name = "branch_id")
    private MerchantBranch merchantBranch;
}
