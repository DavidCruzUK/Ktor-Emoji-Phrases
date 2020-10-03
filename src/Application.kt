package com.lastreact

import com.lastreact.api.*
import com.lastreact.repository.*
import com.lastreact.webapp.*
import freemarker.cache.*
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.features.DefaultHeaders
import io.ktor.freemarker.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

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

    val db = InMemoryRepository()

    routing {
        home()
        about()
        phrases(db)

        // API
        phrase(db)
    }
}

const val API_VERSION = "/api/v1"
