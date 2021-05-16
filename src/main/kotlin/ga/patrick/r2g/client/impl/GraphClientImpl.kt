package ga.patrick.r2g.client.impl

import ga.patrick.r2g.bpp.MeasureTime
import ga.patrick.r2g.client.GraphClient
import ga.patrick.r2g.client.GraphDAO
import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@MeasureTime
@Component
class GraphClientImpl : GraphClient {

    override fun send(uri: String, request: GraphDAO): ResponseEntity<String> {

        val client = WebClient.builder()
                .baseUrl(uri)
                .build()

        val b: Mono<ResponseEntity<String>> = client.post()
                .bodyValue(request)
                .retrieve()
                .onRawStatus({ true }) { Mono.empty() } // ignore any errors, proceed with actual response
                .toEntity(String::class.java)
//                .log()

        return b.block()!!
    }

    companion object : KLogging()
}