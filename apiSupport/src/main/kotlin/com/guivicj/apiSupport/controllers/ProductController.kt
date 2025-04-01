package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.annotations.CurrentUser
import com.guivicj.apiSupport.dtos.ProductDTO
import com.guivicj.apiSupport.dtos.requests.CreateProductRequest
import com.guivicj.apiSupport.dtos.requests.ProductDeleteRequest
import com.guivicj.apiSupport.dtos.requests.ProductUpdateRequest
import com.guivicj.apiSupport.dtos.responses.Response
import com.guivicj.apiSupport.dtos.responses.UserSessionInfoDTO
import com.guivicj.apiSupport.services.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService
) {
    @GetMapping
    fun getAllProducts(): List<ProductDTO> {
        return productService.getAllProducts()
    }

    @GetMapping("/id/{productId}")
    fun getProductById(@PathVariable productId: Long): ProductDTO {
        return productService.getProductById(productId)
    }

    @PostMapping("/add-product")
    fun createProduct(
        @CurrentUser user: UserSessionInfoDTO,
        @RequestBody product: ProductDTO
    ): ResponseEntity<Response> {
        val response = productService.createProduct(user, product)
        return ResponseEntity.status(response.status).body(response)
    }

    @PutMapping("/update-product")
    fun updateProduct(
        @CurrentUser user: UserSessionInfoDTO,
        @RequestBody product: ProductDTO
    ): ResponseEntity<Response> {
        val response = productService.updateProduct(user, product)
        return ResponseEntity.status(response.status).body(response)
    }

    @DeleteMapping("/delete-product/{productId}")
    fun deleteProduct(
        @CurrentUser user: UserSessionInfoDTO,
        @PathVariable productId: Long
    ): ResponseEntity<Response> {
        val response = productService.deleteProduct(user, productId)
        return ResponseEntity.status(response.status).body(response)
    }

}
