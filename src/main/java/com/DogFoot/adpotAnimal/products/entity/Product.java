package com.DogFoot.adpotAnimal.products.entity;

import com.DogFoot.adpotAnimal.order.entity.OrderItem;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    @Column(name = "productId")
    private Long productId;

    @Column(name = "productPrice")
    private int productPrice;

    @Column(name = "productName")
    private String productName;

    @Column(name = "productStock")
    private int productStock;

    @Column(name = "productLike")
    private int productLike;

    // product 연관 관계


    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;

    public Product(Integer category_id, int productPrice, String productName, int productStock,
        int productLike) {
//        this.category = category;
        this.productPrice = productPrice;
        this.productName = productName;
        this.productStock = productStock;
        this.productLike = productLike;
    }

    public void removeStock(int count) {
        if (count > productStock) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.productStock -= count;
    }

    public void addStock(int count) {
        if (count == 0) {
            throw new IllegalArgumentException("올바르지 않은 수입니다.");
        }
        this.productStock += count;
    }

}