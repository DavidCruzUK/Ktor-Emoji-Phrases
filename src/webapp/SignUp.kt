package com.lastreact.webapp

import com.lastreact.*
import com.lastreact.repository.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.locations.*
import io.ktor.routing.*

const val SIGNUP = "/signup"

@KtorExperimentalLocationsAPI
@Location(SIGNUP)
class SignUp(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val error: String = ""
)

@KtorExperimentalLocationsAPI
fun Route.signUp(db: EmojiPhrasesRepository, hashFunction: (String) -> String) {
    get<SignUp> {
        call.redirect(FreeMarkerContent("signup.ftl", null))
    }
}