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
import it.skrape.selects.html5.span
import it.skrape.selects.text
import selikur.com.models.MovieEntry
import selikur.com.models.Schedule
import selikur.com.models.TheaterDetail
import selikur.com.utils.itemize
import selikur.com.utils.queryString

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/theaters")
class Theater {
    @Location("/{id}")
    data class Detail(val id: String)
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

private fun Doc.toTheaterDetail(id: String): TheaterDetail {
    return TheaterDetail(
        id,
        "h4 > span > strong" { findFirst { text } },
        "h4 > span" { findSecond { text } }.substringBefore("TELEPON :").trim(),
        "h4 > span" { findSecond { text } }.substringAfter("TELEPON :").trim(),
        "a.map-link" {
            findFirst {
                eachHref.firstOrNull()
                    ?.queryString("q")
                    ?.split(",")
                    ?.map(String::trim)
                    ?: emptyList()
            }
        },
        "li.list-group-item" {
            findAll {
                map(DocElement::toSchedule)
            }
        }
    )
}

private fun DocElement.toSchedule(): Schedule {
    return Schedule(
        MovieEntry(
            a { findFirst { eachHref.firstOrNull()?.queryString("movie_id").orEmpty() } },
            a { findSecond { text } },
            "img.img-responsive" { findFirst { eachSrc.firstOrNull().orEmpty() } },
            span { findSecond { text } },
            span { findFirst { text } }
        ),
        ".glyphicon-time" { findFirst { parent { text } } },
        ".p_price" { findFirst { text } },
        ".p_date" { findAll { text }.trim() },
        ".p_time" { findFirst { text } }.itemize(" ")
    )
}