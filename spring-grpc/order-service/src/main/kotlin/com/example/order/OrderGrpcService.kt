package com.example.order

import com.example.order.v1.CreateOrderRequest
import com.example.order.v1.CreateOrderResponse
import com.example.order.v1.OrderServiceGrpcKt
import com.example.product.v1.GetProductRequest
import com.example.product.v1.ProductServiceGrpcKt
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrderGrpcService(
    private val productServiceStub: ProductServiceGrpcKt.ProductServiceCoroutineStub
) : OrderServiceGrpcKt.OrderServiceCoroutineImplBase() {

    override suspend fun createOrder(request: CreateOrderRequest): CreateOrderResponse {
        val quantity = request.quantity.coerceAtLeast(1)
        val product = productServiceStub.getProduct(
            GetProductRequest.newBuilder()
                .setProductId(request.productId)
                .build()
        )

        return CreateOrderResponse.newBuilder()
            .setOrderId(UUID.randomUUID().toString())
            .setProductId(product.productId)
            .setProductName(product.name)
            .setQuantity(quantity)
            .setUnitPrice(product.price)
            .setTotalPrice(product.price * quantity)
            .build()
    }
}
