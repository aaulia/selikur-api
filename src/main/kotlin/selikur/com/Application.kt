package selikur.com

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.serialization.*
import selikur.com.plugins.configureRouting

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    install(Locations)
    install(ContentNegotiation) {
        json()
    }

    configureRouting()
}
