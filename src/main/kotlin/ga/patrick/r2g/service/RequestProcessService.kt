package ga.patrick.r2g.service

import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletRequest

interface RequestProcessService {
    fun processRequest(request: HttpServletRequest): ResponseEntity<String>
}