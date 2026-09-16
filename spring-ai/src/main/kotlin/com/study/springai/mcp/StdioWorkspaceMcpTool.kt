package com.study.springai.mcp

import org.springframework.ai.mcp.annotation.McpResource
import org.springframework.ai.mcp.annotation.McpTool
import org.springframework.ai.mcp.annotation.McpToolParam
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path

/**
 * stdio 전용 샘플. 클라이언트가 서버 프로세스를 로컬에서 직접 기동하므로
 * 로컬 파일 접근 같은 머신 종속 도구가 stdio 전송에 적합하다.
 */
@Component
@Profile("stdio")
class StdioWorkspaceMcpTool(
  @Value("\${sample.stdio.root}") root: String,
) {

  private val root: Path = Path.of(root).toAbsolutePath().normalize()

  data class Entry(val name: String, val directory: Boolean, val size: Long)

  @McpTool(name = "list_workspace", description = "작업 디렉터리 하위 파일 목록 조회")
  fun listWorkspace(
    @McpToolParam(description = "루트 기준 상대 경로, 미지정 시 루트", required = false) path: String?,
  ): List<Entry> = Files.list(resolve(path)).use { entries ->
    entries
      .sorted()
      .map { Entry(it.fileName.toString(), Files.isDirectory(it), if (Files.isDirectory(it)) 0 else Files.size(it)) }
      .toList()
  }

  @McpTool(name = "read_workspace_file", description = "작업 디렉터리 하위 텍스트 파일 읽기")
  fun readWorkspaceFile(
    @McpToolParam(description = "루트 기준 상대 경로", required = true) path: String,
    @McpToolParam(description = "최대 읽기 문자 수", required = false) limit: Int?,
  ): String {
    val target = resolve(path)
    require(Files.isRegularFile(target)) { "일반 파일이 아님: $path" }

    return Files.readString(target).take(limit ?: DEFAULT_LIMIT)
  }

  @McpResource(
    uri = "workspace://root",
    name = "workspace-root",
    description = "stdio 서버가 접근 가능한 루트 경로",
    mimeType = "text/plain",
  )
  fun workspaceRoot(): String = root.toString()

  private fun resolve(path: String?): Path {
    val target = root.resolve(path.orEmpty()).normalize()
    require(target.startsWith(root)) { "루트 밖 경로 접근 불가: $path" }

    return target
  }

  companion object {
    private const val DEFAULT_LIMIT = 10_000
  }
}
