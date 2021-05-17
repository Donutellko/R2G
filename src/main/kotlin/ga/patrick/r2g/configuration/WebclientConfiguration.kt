package ga.patrick.r2g.configuration

import ga.patrick.r2g.property.MappingProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebclientConfiguration {

    @Bean
    @Qualifier(FORWARDER_CLIENT)
    fun getForwarderWebClient(
            mappingProperties: MappingProperties): WebClient = WebClient.builder()
            .baseUrl(mappingProperties.defaultEndpoint)
            .build()

    @Bean
    @Qualifier(GRAPH_CLIENT)
    fun getGraphClient(): WebClient = WebClient.builder()
            .build()

    companion object {
        const val FORWARDER_CLIENT = "FORWARDER_CLIENT"
        const val GRAPH_CLIENT = "GRAPH_CLIENT"
    }
}