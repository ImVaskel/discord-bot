package gay.vaskel.common.models

import com.sksamuel.hoplite.*
import com.sksamuel.hoplite.fp.invalid
import com.sksamuel.hoplite.fp.valid
import java.io.File

data class Config(
    val discord: Discord,
    val database: Database,
    val interop: Interop
)

data class Discord(
    val token: String,
    val prefix: String,
    val owner_ids: List<Long>,
    val id: Long,
    val secret: String
)

data class Database(
    val name: String,
    val username: String,
    val password: String
)

data class Interop(
    val port: Int
)

// Hoplite has no support for loading files outside of classpath.
class ExternalFilePropertySource(
    private val filePath: String,
    private val optional: Boolean = false
) : PropertySource {
    override fun node(context: PropertySourceContext): ConfigResult<Node> {
        return if (!File(filePath).exists()) {
            if (optional) Undefined.valid() else ConfigFailure.UnknownSource(filePath).invalid()
        } else {
            val configFile = File(filePath)

            return context.parsers
                .locate(configFile.extension)
                .map { it.load(configFile.inputStream(), filePath) }
        }
    }
}