package com.guivicj.apiSupport.mappers

import com.guivicj.apiSupport.dtos.AdminDTO
import com.guivicj.apiSupport.models.Admin
import com.guivicj.apiSupport.models.UserModel
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

@Mapper(componentModel = "spring")
interface AdminMapper {
    @Mapping(source = "user.id", target = "userId")
    fun toDTO(admin: Admin): AdminDTO

    @Mapping(source = "userId", target = "user", qualifiedByName = ["mapUserIdToUserModel"])
    fun toEntity(dto: AdminDTO): Admin

    @Named("mapUserIdToUserModel")
    fun mapUserIdToUserModel(userId: Long?): UserModel {
        if (userId == null) {
            throw IllegalArgumentException("User ID cannot be null")
        }
        return UserModel(id = userId)
    }
}