package com.example.order

import com.example.product.v1.ProductServiceGrpcKt
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.grpc.client.GrpcChannelFactory

@Configuration
class ProductClient {

    @Bean
    fun productServiceStub(
        channels: GrpcChannelFactory,
        authClientInterceptor: AuthClientInterceptor
    ): ProductServiceGrpcKt.ProductServiceCoroutineStub =
        ProductServiceGrpcKt.ProductServiceCoroutineStub(channels.createChannel("product-service"))
            .withInterceptors(authClientInterceptor)
}
