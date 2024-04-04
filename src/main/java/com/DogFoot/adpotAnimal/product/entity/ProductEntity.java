package com.DogFoot.adpotAnimal.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;


    @Column(name = "category_id")
    private Integer category_id;


    @Column(name = "price")
    private int price;


    @Column(name = "product_name")
    private String product_name;


    @Column(name = "product_stock")
    private int product_stock;


    @Column(name = "likes")
    private int likes;

    public ProductEntity(Integer category_id, int price, String product_name, int product_stock,
                         int like) {
        this.category_id = category_id;
        this.price = price;
        this.product_name = product_name;
        this.product_stock = product_stock;
        this.likes = like;
    }
}