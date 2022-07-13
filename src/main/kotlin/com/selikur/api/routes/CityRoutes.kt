package com.selikur.api.routes

import com.selikur.api.mapper.toCityDetail
import com.selikur.api.mapper.toCityEntry
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import it.skrape.core.htmlDocument
import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.DocElement

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/cities")
class City {
    @Location("")
    data class Listing(val root: City)

    @Location("/{id}")
    data class Detail(val root: City, val id: String)
}

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.cityRouting() {
    get<City.Listing> {
        val cities = skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.list_city.php" }
            response {
                htmlDocument {
                    relaxed = true
                    findAll(".list-group-item")
                        .map(DocElement::toCityEntry)
                }
            }
        }

        call.respond(cities)
    }

    get<City.Detail> { city ->
        val detail = skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.list_theater.php?city_id=${city.id}" }
            response {
                htmlDocument {
                    relaxed = true
                    toCityDetail(city.id)
                }
            }
        }

        call.respond(detail)
    }
}



