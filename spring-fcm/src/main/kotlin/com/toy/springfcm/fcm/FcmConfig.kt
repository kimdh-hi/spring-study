package com.toy.springfcm.fcm

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.io.FileInputStream

@Configuration
class FcmConfig {

  @Value("src/main/resources/fcm/fcm-key.json")
  private lateinit var resource: Resource
  @PostConstruct
  fun initFirebase() {
    FileInputStream(resource.file).use {
      val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(it))
        .build()
      FirebaseApp.initializeApp(options)
    }
  }
}