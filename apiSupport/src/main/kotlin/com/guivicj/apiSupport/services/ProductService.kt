package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.ProductDTO
import com.guivicj.apiSupport.dtos.requests.CreateProductRequest
import com.guivicj.apiSupport.dtos.requests.ProductDeleteRequest
import com.guivicj.apiSupport.dtos.requests.ProductUpdateRequest
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.enums.UserType
import com.guivicj.apiSupport.mappers.ProductMapper
import com.guivicj.apiSupport.models.Product
import com.guivicj.apiSupport.repositories.ProductRepository
import org.apache.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper,
) {
    fun getAllProducts(): List<ProductDTO> {
        return productRepository.findAll().map(productMapper::toDTO)
    }

    fun getProductById(productId: Long): ProductDTO {
        return productMapper.toDTO(productRepository.findById(productId).orElseThrow())
    }

    fun createProduct(createProductRequest: CreateProductRequest): Response {
        return if (createProductRequest.userType == UserType.ADMIN
            && !productRepository.existsById(createProductRequest.product.id)
        ) {
            saveProductInternal(createProductRequest.product, "created")
        } else {
            Response(HttpStatus.SC_UNAUTHORIZED, "Only admins can add products")
        }
    }

    fun updateProduct(productUpdateRequest: ProductUpdateRequest): Response {
        return if (productUpdateRequest.userType == UserType.ADMIN
            && !productRepository.existsById(productUpdateRequest.product.id)
        ) {
            saveProductInternal(productUpdateRequest.product, "updated")
        } else {
            Response(HttpStatus.SC_UNAUTHORIZED, "Only admins can update products")
        }
    }

    fun deleteProduct(productDeleteRequest: ProductDeleteRequest): Response {
        return if (productDeleteRequest.userType == UserType.ADMIN
            && !productRepository.existsById(productDeleteRequest.productId)
        ) {
            productRepository.deleteById(productDeleteRequest.productId)
            Response(HttpStatus.SC_OK, "Product deleted")
        } else {
            Response(HttpStatus.SC_UNAUTHORIZED, "Only admins can remove products")
        }
    }

    private fun saveProductInternal(product: Product, action: String): Response {
        productRepository.save(product)
        return Response(HttpStatus.SC_OK, "Product ${product.name} $action successfully")
    }

}