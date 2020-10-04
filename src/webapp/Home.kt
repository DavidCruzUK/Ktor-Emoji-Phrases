package com.lastreact.webapp

import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

private const val HOME = "/"

@Location(HOME)
class Home

fun Route.home() {
    get<Home> {
        call.respond(FreeMarkerContent("home.ftl", null))
    }
}