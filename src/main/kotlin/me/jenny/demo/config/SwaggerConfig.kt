package me.jenny.demo.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import java.time.LocalDateTime
import java.time.LocalTime

@Configuration
class SwaggerConfig {

    @Bean
    fun demoApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .directModelSubstitute(LocalDateTime::class.java, String::class.java)
                .directModelSubstitute(LocalTime::class.java, String::class.java)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
                .title("TDD with Spring Kotlin API")
                .version("0.0.1-SNAPSHOT")
                .build()
    }
}
