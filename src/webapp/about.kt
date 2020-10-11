package com.lastreact.webapp

import com.lastreact.model.*
import com.lastreact.repository.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

private const val ABOUT = "/about"

@KtorExperimentalLocationsAPI
@Location(ABOUT)
class About

@KtorExperimentalLocationsAPI
fun Route.about(db: Repository) {
    get<About> {
        val user = call.sessions.get<EPSession>()?.let {  db.user(it.userId) }
        call.respond(FreeMarkerContent("about.ftl", mapOf("user" to user)))
    }
}