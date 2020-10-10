package com.lastreact.webapp

import com.lastreact.repository.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

const val SIGNIN = "/signin"

@KtorExperimentalLocationsAPI
@Location(SIGNIN)
data class SignIn(val userId: String = "", val error: String = "")

@KtorExperimentalLocationsAPI
fun Route.signIn(db: Repository, hashFunction: (String) -> String ) {
    get<SignIn> {
        call.respond(FreeMarkerContent("signin.ftl", null))
    }
}