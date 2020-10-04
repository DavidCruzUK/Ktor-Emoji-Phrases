package com.lastreact.api

import com.lastreact.*
import com.lastreact.model.*
import com.lastreact.repository.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val PHRASE_ENDPOINT = "$API_VERSION/phrase"

fun Route.phrase(db: Repository) {
    authenticate("auth") {
        post(PHRASE_ENDPOINT) {
            val request = call.receive<Request>()
            val phrase = db.add(EmojiPhrase(request.emoji, request.phrase))
            call.respond(phrase)
        }
    }
}