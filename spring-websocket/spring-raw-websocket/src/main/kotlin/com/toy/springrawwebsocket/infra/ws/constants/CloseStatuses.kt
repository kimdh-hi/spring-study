package com.toy.springrawwebsocket.infra.ws.constants

import org.springframework.web.socket.CloseStatus

val DUPLICATE_SESSION: CloseStatus = CloseStatus.POLICY_VIOLATION.withReason("duplicate session")
val PONG_TIMED_OUT: CloseStatus = CloseStatus.SESSION_NOT_RELIABLE.withReason("pong timeout")
