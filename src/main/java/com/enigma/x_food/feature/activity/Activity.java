package com.enigma.x_food.feature.activity;

import com.enigma.x_food.constant.EActivity;
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
@Table(name = "activity")
public class Activity extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "activity_id")
    private String activityID;
    @Enumerated(EnumType.STRING)
    @Column(name = "activity", nullable = false)
    private EActivity activity;
}
