package ga.patrick.r2g.service

import ga.patrick.r2g.client.GraphDTO
import org.springframework.http.ResponseEntity

interface GraphService {

    fun send(uri: String, request: GraphDTO): ResponseEntity<String>
}