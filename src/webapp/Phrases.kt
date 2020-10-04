package com.lastreact.webapp

import com.lastreact.model.*
import com.lastreact.repository.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.lang.IllegalArgumentException

const val PHRASES = "/phrases"

fun Route.phrases(db: Repository) {
    authenticate("auth") {
        get(PHRASES) {
            val user = call.authentication.principal as User
            val phrases = db.phrases()
            call.respond(FreeMarkerContent("phrases.ftl",
                mapOf("phrases" to phrases, "displayName" to user.display)))
        }
        post(PHRASES) {
            val params = call.receiveParameters()
            val emoji = params["emoji"] ?: throw IllegalArgumentException("Missing parameter: Emoji")
            val phrase = params["phrase"] ?: throw IllegalArgumentException("Missing parameter: Phrase")
            db.add(EmojiPhrase(emoji, phrase))
            call.respondRedirect(PHRASES)
        }
    }
}