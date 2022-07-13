package com.selikur.api.routes

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import it.skrape.core.htmlDocument
import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.Doc
import it.skrape.selects.DocElement
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div
import com.selikur.api.models.City.Detail
import com.selikur.api.models.City.Entry
import com.selikur.api.models.Theater
import com.selikur.api.utils.queryString

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
                        .map(DocElement::toEntry)
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
                    toDetail(city.id)
                }
            }
        }

        call.respond(detail)
    }
}

private fun DocElement.toEntry(): Entry {
    return Entry(
        div {
            findFirst {
                attribute("onclick")
                    .substringAfter("'")
                    .substringBefore("'")
                    .queryString("city_id")
                    .orEmpty()
            }
        },
        div { findFirst { text } }
    )
}

private fun Doc.toDetail(id: String): Detail {
    return Detail(
        id,
        "ul.navbar-left > li.nav_bar-animate" { 3 { a { 0 { text } } } }.substringAfterLast("-").trim(),
        "div.all      > li.list-group-item" { findAll { map(DocElement::toTheaterEntry) } },
        "div.premiere > li.list-group-item" { findAll { map(DocElement::toTheaterEntry) } },
        "div.imax     > li.list-group-item" { findAll { map(DocElement::toTheaterEntry) } }
    )
}

private fun DocElement.toTheaterEntry(): Theater.Entry {
    return Theater.Entry(
        div {
            findFirst {
                attribute("onclick")
                    .substringAfter("'")
                    .substringBefore("'")
                    .queryString("cinema_id")
                    .orEmpty()
            }
        },
        div { findFirst { text } }
    )
}