package com.selikur.api.mapper

import com.selikur.api.models.City
import com.selikur.api.utils.queryString
import it.skrape.selects.Doc
import it.skrape.selects.DocElement
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div

fun DocElement.toCityEntry(): City.Entry {
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

fun Doc.toCityDetail(id: String): City.Detail {
    return City.Detail(
        id,
        "ul.navbar-left > li.nav_bar-animate" { 3 { a { 0 { text } } } }.substringAfterLast("-").trim(),
        "div.all      > li.list-group-item" { findAll { map(DocElement::toTheaterEntry) } },
        "div.premiere > li.list-group-item" { findAll { map(DocElement::toTheaterEntry) } },
        "div.imax     > li.list-group-item" { findAll { map(DocElement::toTheaterEntry) } }
    )
}