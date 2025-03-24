package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.mappers.ProductMapper
import com.guivicj.apiSupport.repositories.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    productRepository: ProductRepository,
    mapper: ProductMapper
) {

}