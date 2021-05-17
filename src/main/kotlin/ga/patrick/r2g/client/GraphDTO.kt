package ga.patrick.r2g.client

data class GraphDTO(
        val query: String,
        val variables: Map<String, Any>
)
