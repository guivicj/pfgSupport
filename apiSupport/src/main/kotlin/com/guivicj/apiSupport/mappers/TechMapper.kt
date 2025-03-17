package com.guivicj.apiSupport.mappers

import com.guivicj.apiSupport.dtos.TechDTO
import com.guivicj.apiSupport.models.Technician
import com.guivicj.apiSupport.models.UserModel
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface TechMapper {

    @Mapping(source = "userModel.id", target = "userId")
    fun toDTO(tech: Technician): TechDTO

    @Mapping(source = "userId", target = "userModel", qualifiedByName = ["mapUserIdToUserModel"])
    fun toEntity(dto: TechDTO): Technician

    @Named("mapUserIdToUserModel")
    fun mapUserIdToUserModel(userId: Long?): UserModel {
        if (userId == null) {
            throw IllegalArgumentException("User ID cannot be null")
        }
        return UserModel(id = userId)
    }
}

