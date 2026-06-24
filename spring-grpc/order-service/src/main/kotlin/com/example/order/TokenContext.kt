package com.example.order

import io.grpc.Context
import io.grpc.Metadata

object TokenContext {
    val TOKEN_KEY: Context.Key<String> = Context.key("authorization")
    val METADATA_KEY: Metadata.Key<String> =
        Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER)
}
