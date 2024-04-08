package com.DogFoot.adpotAnimal.products.entity;

import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "product_price")
    private int product_price;

    @Column(name = "product_name")
    private String product_name;

    @Column(name = "product_stock")
    private int product_stock;

    @Column(name = "product_like")
    private int product_like;

    // product 연관 관계


    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;

    public Product(Integer category_id, int product_price, String product_name, int product_stock,
        int product_like) {
//        this.category = category;
        this.product_price = product_price;
        this.product_name = product_name;
        this.product_stock = product_stock;
        this.product_like = product_like;
    }

    public void removeStock(int count) {
        if (count > product_stock) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.product_stock -= count;
    }

    public void addStock(int count) {
        if (count == 0) {
            throw new IllegalArgumentException("올바르지 않은 수입니다.");
        }
        this.product_stock += count;
    }

}