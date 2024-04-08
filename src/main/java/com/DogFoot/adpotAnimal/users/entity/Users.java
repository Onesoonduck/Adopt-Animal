package com.DogFoot.adpotAnimal.users.entity;

import com.DogFoot.adpotAnimal.cart.entity.CartEntity;
import com.DogFoot.adpotAnimal.common.BaseEntity;
import com.DogFoot.adpotAnimal.order.entity.Order;
import com.DogFoot.adpotAnimal.users.dto.UsersDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phoneNumber", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "user_role", nullable = false)
    private UsersRole userRole;

    // Users 연관관계
    @OneToMany(mappedBy = "users", cascade =  CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();


    @Builder
    public Users(String userId, String userName, String password, String email, String phoneNumber, UsersRole userRole) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userRole = userRole;
    }

    public UsersDto toDto() {
        UsersDto usersDto = UsersDto.builder()
            .userId(userId)
            .userName(userName)
            .password(password)
            .email(email)
            .phoneNumber(phoneNumber)
            .userRole(userRole)
            .build();
        return usersDto;
    }

    public void updateUsers(Users users) {
        this.userName = users.getUserName();
        this.password = users.getPassword();
        this.email = users.getEmail();
        this.phoneNumber = users.getPhoneNumber();
    }
}
