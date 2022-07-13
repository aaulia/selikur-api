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
import it.skrape.selects.html5.*
import selikur.com.models.MovieDetail
import selikur.com.models.MovieEntry
import selikur.com.utils.itemize
import selikur.com.utils.queryString

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/movies")
class Movie {
    @Location("")
    data class Playing(val root: Movie)

    @Location("/upcoming")
    data class Upcoming(val root: Movie)

    @Location("/{id}")
    data class Detail(val root: Movie, val id: String)
}

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.movieRouting() {
    get<Movie.Playing> {
        val movies = skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/index.php" }
            response {
                htmlDocument {
                    relaxed = true
                    findAll(".grid_movie")
                        .map(DocElement::toMovieEntry)
                }
            }
        }

        call.respond(movies)
    }

    get<Movie.Upcoming> {
        val movies = skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.coming_soon.php" }
            response {
                htmlDocument {
                    relaxed = true
                    findAll(".grid_movie")
                        .map(DocElement::toMovieEntry)
                }
            }
        }

        call.respond(movies)
    }

    get<Movie.Detail> { movie ->
        val detail = skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.movie_details.php?movie_id=${movie.id}" }
            response {
                htmlDocument {
                    relaxed = true
                    toMovieDetail(movie.id)
                }
            }
        }

        call.respond(detail)
    }
}

private fun DocElement.toMovieEntry(): MovieEntry {
    return MovieEntry(
        a { findFirst { eachHref.firstOrNull()?.queryString("movie_id").orEmpty() } },  // id
        ".title" { findFirst { text } },                                                // title
        a { img { findFirst { eachSrc.firstOrNull().orEmpty() } } },                    // image
        ".rating" { a { findFirst { text } } },                                         // rating
        ".rating" { span { findFirst { text } } }                                       // type
    )
}

private fun Doc.toMovieDetail(id: String): MovieDetail {
    return MovieDetail(
        id,                                                                             // id
        "div.col-xs-8 > div" { findFirst { text } },                                    // title
        "img.img-responsive" { findFirst { eachSrc.firstOrNull().orEmpty() } },         // image
        "div.col-xs-3 > img" {                                                          // rating
            findFirst {
                eachSrc.firstOrNull()
                    ?.substringAfterLast("/")
                    ?.substringBeforeLast(".")
                    ?.uppercase()
                    ?.run { if (last().isDigit()) plus("+") else this }
                    ?: ""
            }
        },
        "a.btn-outline" { findFirst { text } },                                         // type
        "div.col-xs-8" { findSecond { div { findFirst { text } } } }.itemize(),         // genre
        ".glyphicon-time" { findFirst { parent { text } } },                            // duration
        button {                                                                        // trailer
            findAll {
                firstOrNull { it.text.contains("TRAILER", ignoreCase = true) }
                    ?.attribute("onclick")
                    ?.substringAfter("'")
                    ?.substringBefore("'")
                    ?.trim()
                    ?: ""
            }
        },
        "#description" { findFirst { text } },                                          // summary
        ".main-content > div:nth-child(4) > p" { 1 { text } }.itemize(),                // producers
        ".main-content > div:nth-child(4) > p" { 3 { text } }.itemize(),                // directors
        ".main-content > div:nth-child(4) > p" { 5 { text } }.itemize(),                // writers
        ".main-content > div:nth-child(4) > p" { 7 { text } }.itemize(),                // casts
        ".main-content > div:nth-child(4) > p" { 9 { text } }.itemize()                 // distributors
    )
}