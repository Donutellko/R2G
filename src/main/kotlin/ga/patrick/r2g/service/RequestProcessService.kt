package ga.patrick.r2g.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RequestProcessService(

) {
    fun processGet(uri: String): ResponseEntity<Any> {
        return ResponseEntity.ok("Hello")
    }
}