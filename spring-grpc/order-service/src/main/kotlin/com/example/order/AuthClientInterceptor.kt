package com.example.order

import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.Metadata
import io.grpc.MethodDescriptor
import org.springframework.stereotype.Component

@Component
class AuthClientInterceptor : ClientInterceptor {

    override fun <Req, Resp> interceptCall(
        method: MethodDescriptor<Req, Resp>,
        callOptions: CallOptions,
        next: Channel
    ): ClientCall<Req, Resp> {
        return object : SimpleForwardingClientCall<Req, Resp>(next.newCall(method, callOptions)) {
            override fun start(responseListener: Listener<Resp>, headers: Metadata) {
                TokenContext.TOKEN_KEY.get()?.takeIf { it.isNotEmpty() }?.let {
                    headers.put(TokenContext.METADATA_KEY, it)
                }
                super.start(responseListener, headers)
            }
        }
    }
}
