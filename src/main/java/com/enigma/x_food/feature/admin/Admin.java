package com.enigma.x_food.feature.admin;

import com.enigma.x_food.feature.role.Role;
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
@Table(name = "admin")
public class Admin extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "admin_id")
    private String adminID;
    @Column(name = "admin_name", nullable = false)
    private String adminName;
    @Column(name = "admin_email", unique = true, nullable = false)
    private String adminEmail;
    @Column(name = "is_super_admin", nullable = false)
    private Boolean isSuperAdmin;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
