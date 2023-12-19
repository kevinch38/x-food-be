package com.enigma.x_food.feature.item;

import com.enigma.x_food.feature.merchant_branch.MerchantBranch;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
@Entity
@Table(name = "item")
public class Item {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(generator = "uuid")
    @Column(name = "item_id")
    private String itemID;
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;
    @Column(name = "category_id", nullable = false, length = 36)
    private String categoryID;
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private MerchantBranch merchantBranch;
    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "image", nullable = false)
    private byte[] image;
    @Column(name = "initial_price", nullable = false, length = 11)
    private Double initialPrice;
    @Column(name = "discounted_price", nullable = false, length = 11)
    private Double discountedPrice;
    @Column(name = "item_stock", nullable = false,  length = 11)
    private Integer itemStock;
    @Column(name = "is_discount", nullable = false,  length = 5)
    private Boolean isDiscounted;
    @Column(name = "is_recommended", nullable = false,  length = 5)
    private Boolean isRecommended;
    @Column(name = "item_description", unique = true,  length = 150)
    private String itemDescription;
}
