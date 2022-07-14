package com.selikur.api.mappers

import com.selikur.api.models.City
import com.selikur.api.utils.queryString
import it.skrape.core.htmlDocument
import it.skrape.fetcher.Result
import it.skrape.selects.DocElement
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div

fun Result.cityDetail(id: String): City.Detail {
    return htmlDocument {
        relaxed = true
        City.Detail(
            id,
            "ul.navbar-left > li.nav_bar-animate" { 3 { a { 0 { text } } } }.substringAfterLast("-").trim(),
            "div.all      > li.list-group-item" { findAll { map(DocElement::theaterEntry) } },
            "div.premiere > li.list-group-item" { findAll { map(DocElement::theaterEntry) } },
            "div.imax     > li.list-group-item" { findAll { map(DocElement::theaterEntry) } }
        )
    }
}

fun Result.cityEntries(): List<City.Entry> {
    return htmlDocument {
        relaxed = true
        findAll(".list-group-item")
            .map(DocElement::cityEntry)
    }
}

private fun DocElement.cityEntry(): City.Entry {
    return City.Entry(
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
