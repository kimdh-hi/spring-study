package com.toy.springmfa.auth.passkey

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Lob
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

@Entity
class PasskeyCredential(
  @Id
  val credentialId: String,

  @Column(nullable = false)
  val userEntityUserId: String,

  @Lob
  @Column(nullable = false)
  val publicKey: ByteArray,

  var signatureCount: Long,

  var uvInitialized: Boolean,

  val transports: String,

  val backupEligible: Boolean,

  var backupState: Boolean,

  @Lob
  val attestationObject: ByteArray?,

  @Lob
  val attestationClientDataJson: ByteArray?,

  val label: String,

  val created: Instant,

  var lastUsed: Instant,
)

interface PasskeyCredentialRepository : JpaRepository<PasskeyCredential, String> {

  fun findByUserEntityUserId(userEntityUserId: String): List<PasskeyCredential>
}
