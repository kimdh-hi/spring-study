package com.example.product

import com.example.product.v1.GetProductRequest
import com.example.product.v1.GetProductResponse
import com.example.product.v1.ProductServiceGrpcKt
import io.grpc.Status
import io.grpc.StatusException
import org.springframework.stereotype.Service

@Service
class ProductGrpcService : ProductServiceGrpcKt.ProductServiceCoroutineImplBase() {

    private val products = mapOf(
        "P100" to Product("P100", "Keyboard", 50_000),
        "P200" to Product("P200", "Mouse", 30_000),
        "P300" to Product("P300", "Monitor", 250_000)
    )

    override suspend fun getProduct(request: GetProductRequest): GetProductResponse {
        val product = products[request.productId]
            ?: throw StatusException(Status.NOT_FOUND.withDescription("product not found: ${request.productId}"))

        return GetProductResponse.newBuilder()
            .setProductId(product.id)
            .setName(product.name)
            .setPrice(product.price)
            .build()
    }

    private data class Product(
        val id: String,
        val name: String,
        val price: Int
    )
}
