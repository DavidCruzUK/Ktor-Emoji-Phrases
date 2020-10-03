package com.lastreact.webapp

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

private const val MAIN = "/"
private const val HELLO = "/hello"

fun Route.home() {
    get(MAIN) {
        with(call) { respondText("hello, World!") }
    }

    get(HELLO) {
        call.respondText("hello Ktor")
    }
}