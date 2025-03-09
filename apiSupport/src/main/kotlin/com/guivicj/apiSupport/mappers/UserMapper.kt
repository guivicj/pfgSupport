package com.guivicj.apiSupport.mappers

import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.models.UserModel
import org.mapstruct.Mapper
import org.springframework.stereotype.Component

@Mapper(componentModel = "spring")
interface UserMapper {
    fun toDTO(user: UserModel): UserDTO
    fun toEntity(dto: UserDTO): UserModel
}