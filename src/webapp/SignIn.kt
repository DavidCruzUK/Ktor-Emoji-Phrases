package com.lastreact.webapp

import com.lastreact.*
import com.lastreact.model.*
import com.lastreact.repository.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

const val SIGNIN = "/signin"

@KtorExperimentalLocationsAPI
@Location(SIGNIN)
data class SignIn(val userId: String = "", var error: String = "")

@KtorExperimentalLocationsAPI
fun Route.signIn(db: Repository, hashFunction: (String) -> String ) {
    post<SignIn> {
        val signInParamteres = call.receive<Parameters>()
        val userId = signInParamteres["userId"] ?: return@post call.redirect(it)
        val password = signInParamteres["password"] ?: return@post call.redirect(it)

        val signInError = SignIn(userId)

        val signIn = when {
            userId.length < MIN_USER_ID_LENGTH -> null
            password.length < MIN_PASSWORD_LENGTH -> null
            !userNameValid(userId) -> null
            else -> db.user(userId, hashFunction(password))
        }

        if (signIn == null) {
            signInError.error = "Invalid User or Password"
            call.redirect(signInError)
        } else {
            call.sessions.set(EPSession(signIn.userId))
            call.redirect(Phrases())
        }
    }

    get<SignIn> {
        val user = call.sessions.get<EPSession>()?.let { db.user(it.userId) }

        if (user != null) {
            call.redirect(Home())
        } else {
            call.respond(FreeMarkerContent("signin.ftl", mapOf("userId" to it.userId, "error" to it.error)))
        }

    }
}