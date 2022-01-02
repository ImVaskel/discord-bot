package gay.vaskel

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import gay.vaskel.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
        configureTemplating()
        configureMonitoring()
        configureSecurity()
    }.start(wait = true)
}
