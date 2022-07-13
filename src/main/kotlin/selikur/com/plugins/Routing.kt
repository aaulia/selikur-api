package selikur.com.plugins

import io.ktor.application.*
import io.ktor.routing.*
import selikur.com.routes.cityRouting
import selikur.com.routes.movieRouting

fun Application.configureRouting() {
    routing {
        movieRouting()
        cityRouting()
    }
}
