package com.example.product

import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status
import org.springframework.stereotype.Component

@Component
class AuthServerInterceptor : ServerInterceptor {

    private val authKey = Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)

    override fun <Req, Resp> interceptCall(
        call: ServerCall<Req, Resp>,
        headers: Metadata,
        next: ServerCallHandler<Req, Resp>
    ): ServerCall.Listener<Req> {
        val token = headers.get(authKey) ?: ""

        if (!token.contains("valid token")) {
            call.close(
                Status.UNAUTHENTICATED.withDescription("token does not contain 'valid token'"),
                Metadata()
            )
            return object : ServerCall.Listener<Req>() {}
        }

        return next.startCall(call, headers)
    }
}
