package com.selikur.api.plugins

import io.ktor.application.*
import io.ktor.routing.*
import com.selikur.api.routes.cityRouting
import com.selikur.api.routes.movieRouting
import com.selikur.api.routes.theaterRouting

fun Application.configureRouting() {
    routing {
        movieRouting()
        cityRouting()
        theaterRouting()
    }
}
