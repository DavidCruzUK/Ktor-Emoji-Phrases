package com.lastreact.webapp

import com.lastreact.*
import com.lastreact.model.*
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.sessions.*

const val SIGNOUT = "/signout"

@KtorExperimentalLocationsAPI
@Location(SIGNOUT)
class SignOut

@KtorExperimentalLocationsAPI
fun Route.signOut() {
    get<SignOut> {
        call.sessions.clear<EPSession>()
        call.redirect(SignIn())
    }
}