package com.selikur.api.routes

import com.selikur.api.mapper.toCityDetail
import com.selikur.api.mapper.toMovieDetail
import com.selikur.api.mapper.toMovieEntry
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
@Location("/movies")
class Movie {
    @Location("")
    data class Playing(val root: Movie)

    @Location("/upcoming")
    data class Upcoming(val root: Movie)

    @Location("/{id}")
    data class Detail(val root: Movie, val id: String)

    @Location("/{movieId}/theaters/{cityId}")
    data class PlayingAt(val root: Movie, val movieId: String, val cityId: String)
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

    get<Movie.PlayingAt> { (_, mId, cId) ->
        val theaters = skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.list_theater.php?find_by=3&movie_id=${mId}&city_id=${cId}" }
            response {
                htmlDocument {
                    relaxed = true
                    toCityDetail(cId)
                }
            }
        }

        call.respond(theaters)
    }
}
