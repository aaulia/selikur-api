package selikur.com.routes

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
import selikur.com.models.CityDetail
import selikur.com.models.CityEntry
import selikur.com.models.TheaterEntry
import selikur.com.utils.queryString

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/cities")
class City {
    @Location("")
    class Listing

    @Location("/{id}")
    data class Detail(val id: String)
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

private fun DocElement.toCityEntry(): CityEntry {
    return CityEntry(
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

private fun Doc.toCityDetail(id: String): CityDetail {
    return CityDetail(
        id,
        "ul.navbar-left > li.nav_bar-animate" { 3 { a { 0 { text } } } }.substringAfterLast("-").trim(),
        "div.all      > li.list-group-item" { findAll { map(DocElement::toTheaterEntry) } },
        "div.premiere > li.list-group-item" { findAll { map(DocElement::toTheaterEntry) } },
        "div.imax     > li.list-group-item" { findAll { map(DocElement::toTheaterEntry) } }
    )
}

private fun DocElement.toTheaterEntry(): TheaterEntry {
    return TheaterEntry(
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