package com.selikur.api.routes

import com.selikur.api.mappers.toCityDetail
import com.selikur.api.mappers.toCityEntry
import com.selikur.api.models.City.Detail
import com.selikur.api.models.City.Entry
import com.selikur.api.utils.cache.Cache
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
@Location("/cities")
class City {
    @Location("")
    data class Listing(val root: City)

    @Location("/{id}")
    data class Detail(val root: City, val id: String)
}

// @formatter:off
private val cacheMap = mapOf(
    City.Listing::class to Cache.NoKey<List<Entry>>(),
    City.Detail::class  to Cache.Keyed<String, Detail>()
)
// @formatter:on

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.cityRouting() {
    @Suppress("UNCHECKED_CAST")
    get<City.Listing> {
        val memory = cacheMap<City.Listing>() as Cache.NoKey<List<Entry>>
        val cities = memory.fetch() ?: skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.list_city.php" }
            response {
                htmlDocument {
                    relaxed = true
                    findAll(".list-group-item")
                        .map(DocElement::toCityEntry)
                        .also(memory::store)
                }
            }
        }

        call.respond(cities)
    }

    @Suppress("UNCHECKED_CAST")
    get<City.Detail> { city ->
        val memory = cacheMap<City.Detail>() as Cache.Keyed<String, Detail>
        val detail = memory.fetch(city.id) ?: skrape(AsyncFetcher) {
            request { url = "https://m.21cineplex.com/gui.list_theater.php?city_id=${city.id}" }
            response {
                htmlDocument {
                    relaxed = true
                    toCityDetail(city.id)
                        .also(memory.store(city.id)::invoke)
                }
            }
        }

        call.respond(detail)
    }
}



