package ga.patrick.r2g.client

data class GraphDAO(
        val query: String,
        val variables: Map<String, Any>
)
