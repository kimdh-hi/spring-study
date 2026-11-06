package com.toy.dpopproofjwt.infra.security

import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.gen.ECKeyGenerator
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.ObjectPostProcessor
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.DPoPProofContext
import org.springframework.security.oauth2.jwt.DPoPProofJwtDecoderFactory
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoderFactory
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.authentication.DPoPAuthenticationProvider
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler
import org.springframework.security.web.header.HeaderWriterFilter
import java.util.UUID

@Configuration
class SecurityConfig {

  @Bean
  fun securityFilterChain(
    http: HttpSecurity,
    nonceProvider: DPoPNonceProvider,
    dPoPProofDecoderFactory: JwtDecoderFactory<DPoPProofContext>,
  ): SecurityFilterChain {
    http {
      csrf { disable() }
      sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
      authorizeHttpRequests {
        authorize("/login", permitAll)
        authorize(anyRequest, authenticated)
      }
      oauth2ResourceServer {
        jwt { }
        dPoP {
          authenticationFailureHandler = AuthenticationEntryPointFailureHandler(DPoPNonceAuthenticationEntryPoint())
        }
      }
      addFilterAfter<HeaderWriterFilter>(DPoPNonceFilter(nonceProvider))
    }

    @Suppress("UNCHECKED_CAST")
    val resourceServerConfigurer =
      OAuth2ResourceServerConfigurer::class.java as Class<OAuth2ResourceServerConfigurer<HttpSecurity>>
    http.getConfigurer(resourceServerConfigurer)
      .withObjectPostProcessor(object : ObjectPostProcessor<DPoPAuthenticationProvider> {
        override fun <O : DPoPAuthenticationProvider> postProcess(provider: O): O =
          provider.apply { setDPoPProofVerifierFactory(dPoPProofDecoderFactory) }
      })

    return http.build()
  }

  @Bean
  fun dPoPNonceProvider(): DPoPNonceProvider = DPoPNonceProvider()

  @Bean
  fun dPoPProofDecoderFactory(nonceProvider: DPoPNonceProvider): JwtDecoderFactory<DPoPProofContext> {
    val nonceValidator = DPoPNonceValidator(nonceProvider)
    // Run after defaults so other proof errors are not reported as use_dpop_nonce
    return DPoPProofJwtDecoderFactory().apply {
      setJwtValidatorFactory { context ->
        DelegatingOAuth2TokenValidator(
          DPoPProofJwtDecoderFactory.DEFAULT_JWT_VALIDATOR_FACTORY.apply(context),
          nonceValidator,
        )
      }
    }
  }

  @Bean
  fun signingKey(): ECKey = ECKeyGenerator(Curve.P_256).keyID(UUID.randomUUID().toString()).generate()

  @Bean
  fun jwtEncoder(signingKey: ECKey): JwtEncoder = NimbusJwtEncoder(ImmutableJWKSet(JWKSet(signingKey)))

  @Bean
  fun jwtDecoder(signingKey: ECKey): JwtDecoder =
    NimbusJwtDecoder.withJwkSource(ImmutableJWKSet(JWKSet(signingKey.toPublicJWK())))
      .jwsAlgorithm(SignatureAlgorithm.ES256)
      .build()

  @Bean
  fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
}
