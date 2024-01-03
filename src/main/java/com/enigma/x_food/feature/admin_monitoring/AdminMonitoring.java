package com.enigma.x_food.feature.admin_monitoring;

import com.enigma.x_food.feature.admin.Admin;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
@Entity
@Table(name = "admin_monitoring")
public class AdminMonitoring extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "admin_monitoring_id")
    private String adminMonitoringID;
    @Column(name = "activity", nullable = false)
    private String activity;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;
}
