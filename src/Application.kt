package com.lastreact

import com.lastreact.api.*
import com.lastreact.model.*
import com.lastreact.model.User
import com.lastreact.repository.*
import com.lastreact.webapp.*
import freemarker.cache.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.features.DefaultHeaders
import io.ktor.freemarker.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import org.h2.engine.*
import java.net.*
import java.util.concurrent.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(DefaultHeaders)

    install(StatusPages) {
        exception<Throwable> { e->
            call.respondText(
                e.localizedMessage,
                ContentType.Text.Plain,
                HttpStatusCode.InternalServerError
            )
        }
    }

    install(ContentNegotiation) {
        gson {
            setPrettyPrinting ()
        }
    }

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Locations)

    install(Sessions) {
        cookie<EPSession>("SESSION") {
            transform(SessionTransportTransformerMessageAuthentication(hasKey))
        }
    }

    val hashFunction = { s: String -> hash(s) }

    DBFactory

    val db = EmojiPhrasesRepository()

    routing {
        // Add static files
        static("/static") {
            // Add images folder
            resource("images")
        }
        home(db)
        about(db)
        phrases(db, hashFunction)
        signIn(db, hashFunction)
        signOut()
        signUp(db, hashFunction)

        // API
        phrase(db)
    }
}

const val API_VERSION = "/api/v1"

@KtorExperimentalLocationsAPI
suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}

fun ApplicationCall.refererHost() = request.header(HttpHeaders.Referrer)?.let { URI.create(it).host }

fun ApplicationCall.securityCode(date: Long, user: User, hashFunction: (String) -> String) = hashFunction(
    "$date:${user.userId}:${request.host()}:${refererHost()}"
)

fun ApplicationCall.verifyCode(date: Long, user: User, code: String, hashFunction: (String) -> String) =
    securityCode(date, user, hashFunction) == code &&
            (System.currentTimeMillis() - date).let { it > 0 && it < TimeUnit.MILLISECONDS.convert(2, TimeUnit.HOURS) }
