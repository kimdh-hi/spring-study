package com.study.springelasticsearch.product.infra

import com.study.springelasticsearch.product.application.ProductService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class ProductSeeder(
  private val productService: ProductService,
) : ApplicationRunner {

  override fun run(args: ApplicationArguments) {
    productService.register("무선 마우스", "저소음 블루투스 무선 마우스. 배터리 6개월", "주변기기", 29000)
    productService.register("게이밍 마우스", "초경량 유선 게이밍 마우스. 감도 조절 가능", "주변기기", 59000)
    productService.register("기계식 키보드", "적축 기계식 키보드. 무선 블루투스 3대 동시 연결", "주변기기", 119000)
    productService.register("노트북 스탠드", "알루미늄 노트북 거치대. 각도 조절", "액세서리", 39000)
    productService.register("USB 허브", "노트북용 7포트 USB 허브. 무선 충전 지원", "액세서리", 45000)
  }
}
