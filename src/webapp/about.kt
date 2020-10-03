package com.lastreact.webapp

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

private const val ABOUT = "/about"

fun Route.about() {
    get(ABOUT) {
        call.respondText {
            "Hello from about"
        }
    }
}