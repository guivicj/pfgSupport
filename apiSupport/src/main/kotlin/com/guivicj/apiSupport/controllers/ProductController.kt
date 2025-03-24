package com.guivicj.apiSupport.controllers

import com.guivicj.apiSupport.dtos.ProductDTO
import com.guivicj.apiSupport.dtos.requests.CreateProductRequest
import com.guivicj.apiSupport.dtos.requests.ProductDeleteRequest
import com.guivicj.apiSupport.dtos.requests.ProductUpdateRequest
import com.guivicj.apiSupport.dtos.responses.Response
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
    fun createProduct(@RequestBody createProductRequest: CreateProductRequest): ResponseEntity<Response> {
        val response = productService.createProduct(createProductRequest)
        return ResponseEntity.status(response.status).body(response)
    }

    @PutMapping("/update-product")
    fun updateProduct(@RequestBody productUpdateRequest: ProductUpdateRequest): ResponseEntity<Response> {
        val response = productService.updateProduct(productUpdateRequest)
        return ResponseEntity.status(response.status).body(response)
    }

    @DeleteMapping("delete-product")
    fun deleteProduct(@RequestBody productDeleteRequest: ProductDeleteRequest): ResponseEntity<Response> {
        val response = productService.deleteProduct(productDeleteRequest)
        return ResponseEntity.status(response.status).body(response)
    }
}