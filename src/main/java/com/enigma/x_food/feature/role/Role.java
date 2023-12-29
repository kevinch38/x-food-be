package com.enigma.x_food.feature.role;

import com.enigma.x_food.constant.ERole;
import com.enigma.x_food.shared.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "role")
public class Role extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "role_id")
    private String roleID;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 100)
    private ERole role;
}
