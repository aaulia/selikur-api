package com.selikur.api.mapper

import com.selikur.api.models.Movie
import com.selikur.api.utils.itemize
import com.selikur.api.utils.queryString
import it.skrape.selects.Doc
import it.skrape.selects.DocElement
import it.skrape.selects.html5.*

fun DocElement.toMovieEntry(): Movie.Entry {
    return Movie.Entry(
        a { findFirst { eachHref.firstOrNull()?.queryString("movie_id").orEmpty() } },  // id
        ".title" { findFirst { text } },                                                // title
        a { img { findFirst { eachSrc.firstOrNull().orEmpty() } } },                    // image
        ".rating" { a { findFirst { text } } },                                         // rating
        ".rating" { span { findFirst { text } } }                                       // type
    )
}

fun Doc.toMovieDetail(id: String): Movie.Detail {
    return Movie.Detail(
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