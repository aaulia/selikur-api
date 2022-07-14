package com.selikur.api.mappers

import com.selikur.api.models.Movie
import com.selikur.api.models.Theater
import com.selikur.api.utils.itemize
import com.selikur.api.utils.queryString
import it.skrape.selects.Doc
import it.skrape.selects.DocElement
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div
import it.skrape.selects.html5.span
import it.skrape.selects.text

fun DocElement.toTheaterEntry(): Theater.Entry {
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

fun Doc.toTheaterDetail(id: String): Theater.Detail {
    return Theater.Detail(
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
                map(DocElement::toTheaterSchedule)
            }
        }
    )
}

fun DocElement.toTheaterSchedule(): Theater.Detail.Schedule {
    return Theater.Detail.Schedule(
        Movie.Entry(
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