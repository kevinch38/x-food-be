package com.enigma.x_food.feature.item_variety;

import com.enigma.x_food.feature.item.Item;
import com.enigma.x_food.feature.variety.Variety;
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
@Table(name = "item_variety")
public class ItemVariety {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "variety_sub_variety_id")
    private String ItemVarietyID;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "variety_id", nullable = false)
    private Variety variety;
}
