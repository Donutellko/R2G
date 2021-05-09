package ga.patrick.r2g.configuration

import ga.patrick.r2g.property.MappingProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class ForwarderConfiguration {

    @Bean
    fun getWebClient(
            mappingProperties: MappingProperties): WebClient = WebClient.builder()
            .baseUrl(mappingProperties.defaultEndpoint)
            .build()
}