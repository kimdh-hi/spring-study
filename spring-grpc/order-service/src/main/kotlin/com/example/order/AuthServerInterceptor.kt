package com.example.order

import io.grpc.Context
import io.grpc.Contexts
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import org.springframework.stereotype.Component

@Component
class AuthServerInterceptor : ServerInterceptor {

    override fun <Req, Resp> interceptCall(
        call: ServerCall<Req, Resp>,
        headers: Metadata,
        next: ServerCallHandler<Req, Resp>
    ): ServerCall.Listener<Req> {
        val token = headers.get(TokenContext.METADATA_KEY) ?: ""
        val ctx = Context.current().withValue(TokenContext.TOKEN_KEY, token)
        return Contexts.interceptCall(ctx, call, headers, next)
    }
}
