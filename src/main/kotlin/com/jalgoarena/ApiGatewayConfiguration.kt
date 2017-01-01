package com.jalgoarena

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "jalgoarena")
open class ApiGatewayConfiguration {
    var apiGatewayUrl: String? = null
}
