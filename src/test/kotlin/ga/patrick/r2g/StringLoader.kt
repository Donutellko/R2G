package ga.patrick.r2g

object StringLoader {
    public fun fromClasspath(path: String): String {
        val root = StringLoader::class.java

        val resource = root.getResource(path)

        return resource!!.readText()
    }
}