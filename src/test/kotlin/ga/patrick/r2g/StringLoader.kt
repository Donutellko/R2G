package ga.patrick.r2g

object StringLoader {
    public fun fromClasspath(path: String): String {
        val root = StringLoader::class.java

        val resource = root.getResource(path)
                ?: throw Exception("Resource not found for path: $path")

        return resource.readText()
    }
}