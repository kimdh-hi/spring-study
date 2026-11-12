package com.toy.springmfa.support

import com.webauthn4j.data.AttestationConveyancePreference
import com.webauthn4j.data.AuthenticatorSelectionCriteria
import com.webauthn4j.data.PublicKeyCredentialCreationOptions
import com.webauthn4j.data.PublicKeyCredentialDescriptor
import com.webauthn4j.data.PublicKeyCredentialParameters
import com.webauthn4j.data.PublicKeyCredentialRequestOptions
import com.webauthn4j.data.PublicKeyCredentialRpEntity
import com.webauthn4j.data.PublicKeyCredentialType
import com.webauthn4j.data.PublicKeyCredentialUserEntity
import com.webauthn4j.data.ResidentKeyRequirement
import com.webauthn4j.data.UserVerificationRequirement
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.data.extension.client.AuthenticationExtensionsClientInputs
import com.webauthn4j.test.authenticator.webauthn.NoneAttestationAuthenticator
import com.webauthn4j.test.authenticator.webauthn.WebAuthnAuthenticatorAdaptor
import com.webauthn4j.test.client.ClientPlatform
import tools.jackson.databind.JsonNode
import tools.jackson.databind.json.JsonMapper
import java.util.Base64

class VirtualPasskey(origin: String) {

  private val platform = ClientPlatform(Origin(origin), WebAuthnAuthenticatorAdaptor(NoneAttestationAuthenticator()))
  private val jsonMapper = JsonMapper()

  fun register(creationOptionsJson: String, label: String): String {
    val json = jsonMapper.readTree(creationOptionsJson)
    val options = PublicKeyCredentialCreationOptions(
      PublicKeyCredentialRpEntity(json["rp"]["id"].asString(), json["rp"]["name"].asString()),
      PublicKeyCredentialUserEntity(
        json["user"]["id"].bytes(),
        json["user"]["name"].asString(),
        json["user"]["displayName"].asString(),
      ),
      DefaultChallenge(json["challenge"].bytes()),
      listOf(PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256)),
      json["timeout"]?.asLong(),
      emptyList(),
      AuthenticatorSelectionCriteria(null, ResidentKeyRequirement.REQUIRED, UserVerificationRequirement.PREFERRED),
      AttestationConveyancePreference.NONE,
      AuthenticationExtensionsClientInputs(),
    )
    val credential = platform.create(options)
    val response = credential.response!!
    return jsonMapper.writeValueAsString(
      mapOf(
        "publicKey" to mapOf(
          "label" to label,
          "credential" to mapOf(
            "id" to credential.id,
            "rawId" to encode(credential.rawId!!),
            "type" to "public-key",
            "response" to mapOf(
              "attestationObject" to encode(response.attestationObject),
              "clientDataJSON" to encode(response.clientDataJSON),
              "transports" to listOf("internal"),
            ),
            "clientExtensionResults" to emptyMap<String, Any>(),
            "authenticatorAttachment" to "platform",
          ),
        ),
      ),
    )
  }

  fun authenticate(requestOptionsJson: String): String {
    val json = jsonMapper.readTree(requestOptionsJson)
    val options = PublicKeyCredentialRequestOptions(
      DefaultChallenge(json["challenge"].bytes()),
      json["timeout"]?.asLong(),
      json["rpId"].asString(),
      json["allowCredentials"].values().map { PublicKeyCredentialDescriptor(PublicKeyCredentialType.PUBLIC_KEY, it["id"].bytes(), null) },
      UserVerificationRequirement.PREFERRED,
      AuthenticationExtensionsClientInputs(),
    )
    val credential = platform.get(options)
    val response = credential.response!!
    return jsonMapper.writeValueAsString(
      mapOf(
        "id" to credential.id,
        "rawId" to encode(credential.rawId!!),
        "type" to "public-key",
        "response" to mapOf(
          "authenticatorData" to encode(response.authenticatorData),
          "clientDataJSON" to encode(response.clientDataJSON),
          "signature" to encode(response.signature),
          "userHandle" to response.userHandle?.let { encode(it) },
        ),
        "clientExtensionResults" to emptyMap<String, Any>(),
        "authenticatorAttachment" to "platform",
      ),
    )
  }

  private fun JsonNode.bytes(): ByteArray = Base64.getUrlDecoder().decode(asString())

  private fun encode(bytes: ByteArray): String = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
}
