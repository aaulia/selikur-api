package com.selikur.api.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*

fun Application.configureContentNegotiation() {
    install(ContentNegotiation) {
        json()
    }
}
