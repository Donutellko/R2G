package ga.patrick.r2g.service

import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletRequest

interface ForwarderService {
    fun send(requestEntity: HttpServletRequest): ResponseEntity<String>
}