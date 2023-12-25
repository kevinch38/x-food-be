package com.enigma.x_food.feature.category;

import com.enigma.x_food.constant.ECategory;
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
@Table(name = "category")
public class Category {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "category_id")
    private String categoryID;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_name", nullable = false, length = 50)
    private ECategory categoryName;
}
