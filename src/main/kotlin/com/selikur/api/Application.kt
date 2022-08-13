package com.selikur.api

import com.selikur.api.plugins.configureContentNegotiation
import com.selikur.api.plugins.configureDefaultHeaders
import com.selikur.api.plugins.configureLocations
import com.selikur.api.plugins.configureRouting
import io.ktor.server.application.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureLocations()
    configureContentNegotiation()
    configureDefaultHeaders()
    configureRouting()
}
