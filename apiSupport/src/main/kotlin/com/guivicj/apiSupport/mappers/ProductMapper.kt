package com.guivicj.apiSupport.mappers

import com.guivicj.apiSupport.dtos.ProductDTO
import com.guivicj.apiSupport.models.Product
import org.mapstruct.InheritInverseConfiguration
import org.mapstruct.Mapper
import org.mapstruct.Mapping


@Mapper(componentModel = "spring")
interface ProductMapper {
    fun toDTO(product: Product): ProductDTO

    @InheritInverseConfiguration
    fun toEntity(dto: ProductDTO): Product

}