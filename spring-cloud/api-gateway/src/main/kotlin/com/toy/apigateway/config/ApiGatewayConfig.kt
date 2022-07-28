package com.toy.apigateway.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ApiGatewayConfig {

    @Bean
    fun routeLocator(builder: RouteLocatorBuilder): RouteLocator {
        return builder.routes {
            route {
                path("/get")
                filters {
                    addRequestHeader("myHeaderKey", "myHeaderValue")
                    addRequestParameter("myParamKey", "myParamValue")
                }
                uri("http://httpbin.org:80")
            }
            route {
                path("/currency-exchange/**")
                uri("lb://CURRENCY-EXCHANGE") // for Load Balancing (eureka server 에 등록된 서비스의 이름)
                                              // /CURRENCY-EXCHANGE/currency-exchange/...
            }
            route {
                path("/currency-conversion/feign/**")
                uri("lb://CURRENCY-CONVERSION")
            }
            // 특정 요청 url을 다른 url로 요청
            route {
                path("/my-currency-conversion/feign/**")
                filters {
                    rewritePath(
                        "/my-currency-conversion/feign", // 요청받은 url
                        "/currency-conversion/feign" // 실제 요청되는 url
                    )
                }
                uri("lb://CURRENCY-CONVERSION")
            }
        }
    }
}