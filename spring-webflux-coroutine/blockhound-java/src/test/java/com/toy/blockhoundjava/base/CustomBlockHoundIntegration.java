package com.toy.blockhoundjava.base;

import com.google.auto.service.AutoService;
import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;

@AutoService(BlockHoundIntegration.class)
public class CustomBlockHoundIntegration implements BlockHoundIntegration {
  @Override
  public void applyTo(BlockHound.Builder builder) {
    builder
      .allowBlockingCallsInside("java.io.FileInputStream", "readBytes")
      .allowBlockingCallsInside("java.util.UUID", "randomUUID");
  }
}