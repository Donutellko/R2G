package ga.patrick.r2g.util

import javax.servlet.http.HttpServletRequest

val HttpServletRequest.fullUri: String
        get() = if (this.queryString.isNullOrBlank()) {
                this.requestURI
        } else {
                this.requestURI + "?" + this.queryString
        }