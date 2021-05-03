package ga.patrick.r2g.client

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component
class GraphClient {

    fun send(uri: String, query: String): ResponseEntity<String> {
        val client = WebClient.builder()
                .baseUrl(uri)
                .build()

        val request = GraphDAO(query = query)

        return client.post()
                .bodyValue(request)
                .retrieve()
                .toEntity(String::class.java)
                .block()!!
    }
}