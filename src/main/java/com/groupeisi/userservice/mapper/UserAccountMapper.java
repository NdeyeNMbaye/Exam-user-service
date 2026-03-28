package com.groupeisi.userservice.mapper;

import com.groupeisi.userservice.dto.UserAccountDto;
import com.groupeisi.userservice.entities.UserAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {
    UserAccountDto toDto(UserAccount userAccount);
    UserAccount toEntity(UserAccountDto dto);
}