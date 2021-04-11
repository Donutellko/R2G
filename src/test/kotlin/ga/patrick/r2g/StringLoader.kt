package ga.patrick.r2g

import java.io.File

object StringLoader {
    public fun fromClasspath(path: String): String = File(path).readText()
}