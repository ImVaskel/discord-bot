package gay.vaskel.dashboard.plugins

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.velocity.*
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader

fun Application.configureTemplating() {
    install(Velocity) {
        setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath")
        setProperty("classpath.resource.loader.class", ClasspathResourceLoader::class.java.name)
    }
    routing {
    }
}
