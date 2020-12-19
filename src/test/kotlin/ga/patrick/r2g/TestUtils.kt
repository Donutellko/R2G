package ga.patrick.r2g

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val MAPPER = jacksonObjectMapper()

fun Any.toJson(): String = MAPPER.writeValueAsString(this)