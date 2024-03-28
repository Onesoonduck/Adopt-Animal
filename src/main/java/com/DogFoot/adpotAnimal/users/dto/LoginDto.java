package com.DogFoot.adpotAnimal.users.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginDto {

    private String userId;
    private String password;
}
