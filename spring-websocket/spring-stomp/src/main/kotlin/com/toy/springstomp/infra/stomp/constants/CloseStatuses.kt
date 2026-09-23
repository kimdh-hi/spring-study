package com.toy.springstomp.infra.stomp.constants

import org.springframework.web.socket.CloseStatus

val DUPLICATE_SESSION: CloseStatus = CloseStatus.POLICY_VIOLATION.withReason("duplicate session")
