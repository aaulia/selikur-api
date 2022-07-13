package selikur.com.plugins

import io.ktor.application.*
import io.ktor.features.*

fun Application.configureDefaultHeaders() {
    install(DefaultHeaders)
}