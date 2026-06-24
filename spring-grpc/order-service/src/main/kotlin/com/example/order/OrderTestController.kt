package com.example.order

import com.example.product.v1.GetProductRequest
import com.example.product.v1.ProductServiceGrpcKt
import io.grpc.Metadata
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/test")
class OrderTestController(
    private val productServiceStub: ProductServiceGrpcKt.ProductServiceCoroutineStub
) {

    @PostMapping("/orders")
    fun createOrder(
        @RequestBody request: TestCreateOrderRequest,
        @RequestHeader(name = "authorization", required = false) authorization: String?
    ): TestCreateOrderResponse = runBlocking {
        val quantity = request.quantity.coerceAtLeast(1)
        val headers = Metadata().apply {
            put(TokenContext.METADATA_KEY, authorization?.takeIf { it.isNotBlank() } ?: "valid token")
        }
        val product = productServiceStub.getProduct(
            GetProductRequest.newBuilder()
                .setProductId(request.productId)
                .build(),
            headers
        )

        TestCreateOrderResponse(
            orderId = UUID.randomUUID().toString(),
            productId = product.productId,
            productName = product.name,
            quantity = quantity,
            unitPrice = product.price,
            totalPrice = product.price * quantity
        )
    }
}

data class TestCreateOrderRequest(
    val productId: String = "P100",
    val quantity: Int = 1
)

data class TestCreateOrderResponse(
    val orderId: String,
    val productId: String,
    val productName: String,
    val quantity: Int,
    val unitPrice: Int,
    val totalPrice: Int
)
