package selikur.com

import io.ktor.application.*
import selikur.com.plugins.configureContentNegotiation
import selikur.com.plugins.configureDefaultHeaders
import selikur.com.plugins.configureLocations
import selikur.com.plugins.configureRouting

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureLocations()
    configureContentNegotiation()
    configureDefaultHeaders()
    configureRouting()
}
