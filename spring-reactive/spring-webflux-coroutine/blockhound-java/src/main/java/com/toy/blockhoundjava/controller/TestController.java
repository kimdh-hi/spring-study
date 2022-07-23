package com.toy.blockhoundjava.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/test")
public class TestController {

  @GetMapping("/block")
  public ResponseEntity<String> block() {
    try {
      Thread.sleep(1000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return ResponseEntity.ok("ok");
  }

  @GetMapping("/non-block")
  public ResponseEntity<String> nonBlock() {
    return ResponseEntity.ok("ok");
  }

  @GetMapping("/allow-block")
  public ResponseEntity<String> allowBlock() {
    String uuid = UUID.randomUUID().toString();
    return ResponseEntity.ok(uuid);
  }
}
