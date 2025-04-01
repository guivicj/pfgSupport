package com.guivicj.apiSupport.services

import com.guivicj.apiSupport.dtos.ProductDTO
import com.guivicj.apiSupport.dtos.requests.CreateProductRequest
import com.guivicj.apiSupport.dtos.requests.ProductDeleteRequest
import com.guivicj.apiSupport.dtos.requests.ProductUpdateRequest
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
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

    fun createProduct(currentUser: UserSessionInfoDTO, product: ProductDTO): Response {
        if (currentUser.user.type != UserType.ADMIN) {
            return Response(HttpStatus.SC_UNAUTHORIZED, "Only ADMINs can add products")
        }

        if (productRepository.existsByName(product.name)) {
            return Response(HttpStatus.SC_CONFLICT, "Product already exists")
        }

        return saveProductInternal(productMapper.toEntity(product), "created")
    }

    fun updateProduct(currentUser: UserSessionInfoDTO, product: ProductDTO): Response {
        if (currentUser.user.type != UserType.ADMIN) {
            return Response(HttpStatus.SC_UNAUTHORIZED, "Only ADMINSs can update products")
        }

        if (!productRepository.existsByName(product.name)) {
            return Response(HttpStatus.SC_NOT_FOUND, "Product not found")
        }

        return saveProductInternal(productMapper.toEntity(product), "updated")
    }


    fun deleteProduct(currentUser: UserSessionInfoDTO, productId: Long): Response {
        if (currentUser.user.type != UserType.ADMIN) {
            return Response(HttpStatus.SC_UNAUTHORIZED, "Only ADMINs can remove products")
        }

        if (!productRepository.existsById(productId)) {
            return Response(HttpStatus.SC_NOT_FOUND, "Product not found")
        }

        productRepository.deleteById(productId)
        return Response(HttpStatus.SC_OK, "Product deleted")
    }


    private fun saveProductInternal(product: Product, action: String): Response {
        productRepository.save(product)
        return Response(HttpStatus.SC_OK, "Product ${product.name} $action successfully")
    }

}
