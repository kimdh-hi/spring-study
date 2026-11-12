package com.toy.springmfa.auth.passkey

import java.time.Clock
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class PendingCeremonyStore<T : Any>(
  private val ttl: Duration,
  private val clock: Clock,
) {

  private val pending = ConcurrentHashMap<String, Pair<T, Instant>>()

  fun save(username: String, options: T) {
    pending[username] = options to clock.instant().plus(ttl)
  }

  fun take(username: String): T? {
    val (options, expiresAt) = pending.remove(username) ?: return null
    return options.takeIf { clock.instant().isBefore(expiresAt) }
  }
}
