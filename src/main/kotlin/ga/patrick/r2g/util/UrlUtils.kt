package ga.patrick.r2g.util

import javax.servlet.http.HttpServletRequest

val HttpServletRequest.fullUri: String
        get() = this.requestURI + "?" + this.queryString