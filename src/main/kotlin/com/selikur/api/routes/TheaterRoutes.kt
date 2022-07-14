package com.selikur.api.routes

import com.selikur.api.mappers.theaterDetail
import com.selikur.api.models.Theater.Detail
import com.selikur.api.utils.cache.Cache
import com.selikur.api.utils.cache.CacheMap
import com.selikur.api.utils.cache.invoke
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import it.skrape.fetcher.AsyncFetcher
import it.skrape.fetcher.extract
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape

@OptIn(KtorExperimentalLocationsAPI::class)
@Location("/theaters")
class Theater {
    @Location("/{id}")
    data class Detail(val root: Theater, val id: String)
}

// @formatter:off
private val cacheMap: CacheMap = mapOf(
    Theater.Detail::class to Cache.Keyed<String, Detail>()
)
// @formatter:on

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.theaterRouting() {
    @Suppress("UNCHECKED_CAST")
    get<Theater.Detail> { theater ->
        val memory = cacheMap<Theater.Detail>() as Cache.Keyed<String, Detail>
        val detail = memory.fetch(theater.id) ?: skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.schedule.php?find_by=1&cinema_id=${theater.id}" }
            extract { memory.store(theater.id, theaterDetail(theater.id)) }
        }

        call.respond(detail)
    }
}
