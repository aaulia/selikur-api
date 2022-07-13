package com.selikur.api.utils

fun String.queryString(field: String): String? {
    return substringAfterLast("?")
        .split("&")
        .flatMap { queries -> queries.split("=") }
        .zipWithNext()
        .firstOrNull { (key, _) -> key == field }
        ?.second
}

fun String.itemize(delimiters: String = ","): List<String> {
    return split(delimiters).map(String::trim)
}
