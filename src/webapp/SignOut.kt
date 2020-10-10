package com.lastreact.webapp

import com.lastreact.*
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.routing.*

const val SIGNOUT = "/signout"

@KtorExperimentalLocationsAPI
@Location(SIGNOUT)
class SignOut

@KtorExperimentalLocationsAPI
fun Route.signOut() {
    get<SignOut> {
        call.redirect(SignIn())
    }
}