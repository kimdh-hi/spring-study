package com.study.springai.mcp

import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.ai.mcp.annotation.McpToolParam
import org.springframework.stereotype.Component

@Component
class GreetingMcpTool {

  @McpTool(name = "greet", description = "이름으로 인사말 생성")
  fun greet(@McpToolParam(description = "인사할 이름", required = true) name: String) = "안녕하세요, $name!"
}