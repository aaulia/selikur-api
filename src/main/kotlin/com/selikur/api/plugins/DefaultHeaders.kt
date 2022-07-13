package com.selikur.api.plugins

import io.ktor.application.*
import io.ktor.features.*

fun Application.configureDefaultHeaders() {
    install(DefaultHeaders)
}