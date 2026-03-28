package com.groupeisi.userservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAccountDto {
    private Long id;
    private String email;
    private String password;
    private String role;
}