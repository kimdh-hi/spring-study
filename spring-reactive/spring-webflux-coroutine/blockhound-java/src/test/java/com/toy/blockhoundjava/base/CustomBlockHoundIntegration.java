package com.toy.blockhoundjava.base;

import com.google.auto.service.AutoService;
import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;

import java.io.FileInputStream;

@AutoService(BlockHoundIntegration.class)
public class CustomBlockHoundIntegration implements BlockHoundIntegration {
  @Override
  public void applyTo(BlockHound.Builder builder) {
    builder
      .allowBlockingCallsInside(FileInputStream.class.getName(), "readBytes")
      .allowBlockingCallsInside(FileInputStream.class.getName(), "read");
  }
}