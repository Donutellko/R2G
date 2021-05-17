package ga.patrick.r2g.service.impl

import ga.patrick.r2g.bpp.MeasureTime
import ga.patrick.r2g.service.GraphService
import ga.patrick.r2g.client.GraphDTO
import ga.patrick.r2g.configuration.WebclientConfiguration.Companion.GRAPH_CLIENT
import mu.KLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@MeasureTime
@Component
class GraphServiceImpl(
        @Qualifier(GRAPH_CLIENT)
        val client: WebClient
) : GraphService {

    override fun send(uri: String, request: GraphDTO): ResponseEntity<String> {

        val b: Mono<ResponseEntity<String>> = client.post()
                .uri(uri)
                .bodyValue(request)
                .retrieve()
                .onRawStatus({ true }) { Mono.empty() } // ignore any errors, proceed with actual response
                .toEntity(String::class.java)

        return b.block()!!
    }

    companion object : KLogging()
}