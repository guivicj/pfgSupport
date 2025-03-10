package com.guivicj.apiSupport.mappers

import com.guivicj.apiSupport.dtos.UserDTO
import com.guivicj.apiSupport.models.UserModel
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface UserMapper {
    fun toDTO(user: UserModel): UserDTO
    @InheritInverseConfiguration
    fun toEntity(dto: UserDTO): UserModel
}