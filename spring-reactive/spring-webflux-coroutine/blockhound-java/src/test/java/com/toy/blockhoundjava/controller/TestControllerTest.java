package com.toy.blockhoundjava.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestControllerTest {

  @Autowired
  private WebTestClient client;

  @BeforeAll
  void beforeAll() {
    BlockHound.install();
  }

  @DisplayName("call block api")
  @Test
  void block() {
    client.get()
      .uri("/test/block")
      .exchange()
      .expectStatus().is5xxServerError();
  }

  @DisplayName("call non-block api")
  @Test
  void nonBlock() {
    client.get()
      .uri("/test/non-block")
      .exchange()
      .expectStatus().isOk();
  }

  @DisplayName("call allow-block api")
  @Test
  void allowBlock() {
    client.get()
      .uri("/test/allow-block")
      .exchange()
      .expectStatus().isOk();
  }

}