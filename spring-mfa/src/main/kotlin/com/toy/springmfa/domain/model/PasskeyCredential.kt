package com.toy.springmfa.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Lob
import java.time.Instant

@Entity
class PasskeyCredential(
  @Id
  @Column(nullable = false, length = 1364)
  val credentialId: String,

  @Column(nullable = false, length = 86)
  val userEntityUserId: String,

  @Lob
  @Column(nullable = false)
  val publicKey: ByteArray,

  @Column(nullable = false)
  var signatureCount: Long,

  @Column(nullable = false)
  var uvInitialized: Boolean,

  @Column(nullable = false, length = 100)
  val transports: String,

  @Column(nullable = false)
  val backupEligible: Boolean,

  @Column(nullable = false)
  var backupState: Boolean,

  @Lob
  @Column(nullable = true)
  val attestationObject: ByteArray?,

  @Lob
  @Column(nullable = true)
  val attestationClientDataJson: ByteArray?,

  @Column(nullable = false, length = 255)
  val label: String,

  @Column(nullable = false)
  val created: Instant,

  @Column(nullable = false)
  var lastUsed: Instant,
)
