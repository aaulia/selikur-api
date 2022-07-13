package com.selikur.api.routes

import com.selikur.api.mapper.toTheaterDetail
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import it.skrape.core.htmlDocument
import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/theaters")
class Theater {
    @Location("/{id}")
    data class Detail(val root: Theater, val id: String)
}

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.theaterRouting() {
    get<Theater.Detail> { theater ->
        val detail = skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.schedule.php?find_by=1&cinema_id=${theater.id}" }
            response {
                htmlDocument {
                    relaxed = true
                    toTheaterDetail(theater.id)
                }
            }
        }

        call.respond(detail)
    }
}
