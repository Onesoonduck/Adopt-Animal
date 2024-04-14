package com.DogFoot.adpotAnimal.users.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UsersTableDto {
    private String userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private int orderCount;

    @Builder
    public UsersTableDto(String userId, String userName, String email, String phoneNumber, LocalDateTime createdAt, int orderCount) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.orderCount = orderCount;
    }
}
