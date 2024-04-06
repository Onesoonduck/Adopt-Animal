package com.DogFoot.adpotAnimal.products.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;


    @Column(name = "category_id")
    private Integer category_id;


    @Column(name = "product_price")
    private int product_price;


    @Column(name = "product_name")
    private String product_name;


    @Column(name = "product_stock")
    private int product_stock;


    @Column(name = "product_like")
    private int product_like;

    public Product(Integer category_id, int product_price, String product_name, int product_stock,
        int product_like) {
        this.category_id = category_id;
        this.product_price = product_price;
        this.product_name = product_name;
        this.product_stock = product_stock;
        this.product_like = product_like;
    }
}