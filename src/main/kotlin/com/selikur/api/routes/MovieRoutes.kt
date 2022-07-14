package com.selikur.api.routes

import com.selikur.api.mappers.toCityDetail
import com.selikur.api.mappers.toMovieDetail
import com.selikur.api.mappers.toMovieEntry
import com.selikur.api.models.City
import com.selikur.api.models.Movie.Detail
import com.selikur.api.models.Movie.Entry
import com.selikur.api.utils.cache.Cache
import com.selikur.api.utils.cache.CacheMap
import com.selikur.api.utils.cache.invoke
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

// @formatter:off
private val cacheMap: CacheMap = mapOf(
    Movie.Playing::class   to Cache.NoKey<List<Entry>>(),
    Movie.Upcoming::class  to Cache.NoKey<List<Entry>>(),
    Movie.Detail::class    to Cache.Keyed<String, Detail>(),
    Movie.PlayingAt::class to Cache.Keyed<Pair<String, String>, List<City.Detail>>()
)
// @formatter:on

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.movieRouting() {
    @Suppress("UNCHECKED_CAST")
    get<Movie.Playing> {
        val memory = cacheMap<Movie.Playing>() as Cache.NoKey<List<Entry>>
        val movies = memory.fetch() ?: skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/index.php" }
            response {
                htmlDocument {
                    relaxed = true
                    findAll(".grid_movie")
                        .map(DocElement::toMovieEntry)
                        .also(memory::store)
                }
            }
        }

        call.respond(movies)
    }

    @Suppress("UNCHECKED_CAST")
    get<Movie.Upcoming> {
        val memory = cacheMap<Movie.Upcoming>() as Cache.NoKey<List<Entry>>
        val movies = memory.fetch() ?: skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.coming_soon.php" }
            response {
                htmlDocument {
                    relaxed = true
                    findAll(".grid_movie")
                        .map(DocElement::toMovieEntry)
                        .also(memory::store)
                }
            }
        }

        call.respond(movies)
    }

    @Suppress("UNCHECKED_CAST")
    get<Movie.Detail> { movie ->
        val memory = cacheMap<Movie.Detail>() as Cache.Keyed<String, Detail>
        val detail = memory.fetch(movie.id) ?: skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.movie_details.php?movie_id=${movie.id}" }
            response {
                htmlDocument {
                    relaxed = true
                    toMovieDetail(movie.id)
                        .also(memory.store(movie.id)::invoke)
                }
            }
        }

        call.respond(detail)
    }

    @Suppress("UNCHECKED_CAST")
    get<Movie.PlayingAt> { (_, mId, cId) ->
        val idPair = mId to cId
        val memory = cacheMap<Movie.PlayingAt>() as Cache.Keyed<Pair<String, String>, City.Detail>
        val theaters = memory.fetch(idPair) ?: skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.list_theater.php?find_by=3&movie_id=${mId}&city_id=${cId}" }
            response {
                htmlDocument {
                    relaxed = true
                    toCityDetail(cId)
                        .also(memory.store(idPair)::invoke)
                }
            }
        }

        call.respond(theaters)
    }
}
