package com.lastreact.webapp

import com.lastreact.model.*
import com.lastreact.repository.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

private const val HOME = "/"

@KtorExperimentalLocationsAPI
@Location(HOME)
class Home

@KtorExperimentalLocationsAPI
fun Route.home(db: Repository) {
    get<Home> {
        val user = call.sessions.get<EPSession>()?.let {  db.user(it.userId) }
        call.respond(FreeMarkerContent("home.ftl", mapOf("user" to user)))
    }
}