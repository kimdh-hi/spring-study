package com.study.springgraalvm.preset

import com.study.springgraalvm.application.CompanyService
import com.study.springgraalvm.domain.repository.CompanyRepository
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

@Component
@RegisterReflectionForBinding(CompanyPreset::class)
class CompanyPresetLoader(
  private val objectMapper: ObjectMapper,
  private val companyRepository: CompanyRepository,
  private val companyService: CompanyService,
) : ApplicationRunner {

  override fun run(args: ApplicationArguments) {
    if (companyRepository.findAll().isNotEmpty()) return

    val presets = ClassPathResource("presets/companies.json").inputStream.use {
      objectMapper.readValue(it, object : TypeReference<List<CompanyPreset>>() {})
    }
    presets.forEach { companyService.create(it.name) }
  }
}
