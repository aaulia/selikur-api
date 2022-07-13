package selikur.com.plugins

import io.ktor.application.*
import io.ktor.routing.*
import selikur.com.routes.cityRouting
import selikur.com.routes.movieRouting
import selikur.com.routes.theaterRouting

fun Application.configureRouting() {
    routing {
        movieRouting()
        cityRouting()
        theaterRouting()
    }
}
