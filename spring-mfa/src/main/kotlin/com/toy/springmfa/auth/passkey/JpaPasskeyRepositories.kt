package com.toy.springmfa.auth.passkey

import org.springframework.security.web.webauthn.api.AuthenticatorTransport
import org.springframework.security.web.webauthn.api.Bytes
import org.springframework.security.web.webauthn.api.CredentialRecord
import org.springframework.security.web.webauthn.api.ImmutableCredentialRecord
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCose
import org.springframework.security.web.webauthn.api.ImmutablePublicKeyCredentialUserEntity
import org.springframework.security.web.webauthn.api.PublicKeyCredentialType
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository
import org.springframework.security.web.webauthn.management.UserCredentialRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class JpaPublicKeyCredentialUserEntityRepository(
  private val passkeyUserRepository: PasskeyUserRepository,
) : PublicKeyCredentialUserEntityRepository {

  override fun findById(id: Bytes): PublicKeyCredentialUserEntity? =
    passkeyUserRepository.findById(id.toBase64UrlString()).map { it.toUserEntity() }.orElse(null)

  override fun findByUsername(username: String): PublicKeyCredentialUserEntity? =
    passkeyUserRepository.findByUsername(username)?.toUserEntity()

  override fun save(userEntity: PublicKeyCredentialUserEntity) {
    passkeyUserRepository.save(
      PasskeyUser(userEntity.id.toBase64UrlString(), userEntity.name, userEntity.displayName ?: userEntity.name),
    )
  }

  override fun delete(id: Bytes) {
    passkeyUserRepository.deleteById(id.toBase64UrlString())
  }

  private fun PasskeyUser.toUserEntity(): PublicKeyCredentialUserEntity =
    ImmutablePublicKeyCredentialUserEntity.builder()
      .id(Bytes.fromBase64(id))
      .name(username)
      .displayName(displayName)
      .build()
}

@Component
@Transactional
class JpaUserCredentialRepository(
  private val passkeyCredentialRepository: PasskeyCredentialRepository,
) : UserCredentialRepository {

  override fun findByCredentialId(credentialId: Bytes): CredentialRecord? =
    passkeyCredentialRepository.findById(credentialId.toBase64UrlString()).map { it.toRecord() }.orElse(null)

  override fun findByUserId(userId: Bytes): List<CredentialRecord> =
    passkeyCredentialRepository.findByUserEntityUserId(userId.toBase64UrlString()).map { it.toRecord() }

  override fun save(record: CredentialRecord) {
    passkeyCredentialRepository.save(
      PasskeyCredential(
        credentialId = record.credentialId.toBase64UrlString(),
        userEntityUserId = record.userEntityUserId.toBase64UrlString(),
        publicKey = record.publicKey.bytes,
        signatureCount = record.signatureCount,
        uvInitialized = record.isUvInitialized,
        transports = record.transports.joinToString(",") { it.value },
        backupEligible = record.isBackupEligible,
        backupState = record.isBackupState,
        attestationObject = record.attestationObject?.bytes,
        attestationClientDataJson = record.attestationClientDataJSON?.bytes,
        label = record.label,
        created = record.created,
        lastUsed = record.lastUsed,
      ),
    )
  }

  override fun delete(credentialId: Bytes) {
    passkeyCredentialRepository.deleteById(credentialId.toBase64UrlString())
  }

  private fun PasskeyCredential.toRecord(): CredentialRecord =
    ImmutableCredentialRecord.builder()
      .credentialType(PublicKeyCredentialType.PUBLIC_KEY)
      .credentialId(Bytes.fromBase64(credentialId))
      .userEntityUserId(Bytes.fromBase64(userEntityUserId))
      .publicKey(ImmutablePublicKeyCose(publicKey))
      .signatureCount(signatureCount)
      .uvInitialized(uvInitialized)
      .transports(transports.split(",").filter { it.isNotEmpty() }.map { AuthenticatorTransport.valueOf(it) }.toSet())
      .backupEligible(backupEligible)
      .backupState(backupState)
      .attestationObject(attestationObject?.let { Bytes(it) })
      .attestationClientDataJSON(attestationClientDataJson?.let { Bytes(it) })
      .label(label)
      .created(created)
      .lastUsed(lastUsed)
      .build()
}
