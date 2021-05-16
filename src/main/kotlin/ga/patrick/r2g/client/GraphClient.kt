package ga.patrick.r2g.client

import org.springframework.http.ResponseEntity

interface GraphClient {

    fun send(uri: String, request: GraphDAO): ResponseEntity<String>
}