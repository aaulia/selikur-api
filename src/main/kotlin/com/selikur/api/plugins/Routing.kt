package com.selikur.api.plugins

import com.selikur.api.routes.cityRouting
import com.selikur.api.routes.movieRouting
import com.selikur.api.routes.theaterRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        movieRouting()
        cityRouting()
        theaterRouting()
    }
}
