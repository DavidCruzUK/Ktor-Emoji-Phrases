package com.lastreact.webapp

import com.lastreact.*
import com.lastreact.model.*
import com.lastreact.repository.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.sessions.*

const val SIGNUP = "/signup"

@KtorExperimentalLocationsAPI
@Location(SIGNUP)
class SignUp(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    var error: String = ""
)

@KtorExperimentalLocationsAPI
fun Route.signUp(db: EmojiPhrasesRepository, hashFunction: (String) -> String) {
    post<SignUp> {
        val user = call.sessions.get<EPSession>()?.let { db.user(userId = it.userId) }
        if (user != null) return@post call.redirect(Phrases())

        val signupParameters = call.receive<Parameters>()
        val userId = signupParameters["userId"] ?: return@post call.redirect(it)
        val password = signupParameters["password"] ?: return@post call.redirect(it)
        val displayName = signupParameters["displayName"] ?: return@post call.redirect(it)
        val email = signupParameters["email"] ?: return@post call.redirect(it)

        val signUpError = SignUp(userId, displayName, email)

        when {
            password.length < MIN_PASSWORD_LENGTH -> {
                signUpError.error = "Password should be at least $MIN_PASSWORD_LENGTH characters long"
                call.redirect(signUpError)
            }
            userId.length < MIN_USER_ID_LENGTH -> {
                signUpError.error = "Username should be at least $MIN_USER_ID_LENGTH characters long"
                call.redirect(signUpError)
            }
            !userNameValid(userId) -> {
                signUpError.error = "Username consist of digits, letters, dots or underscores"
                call.redirect(signUpError)
            }
            !userNameValid(userId) -> {
                signUpError.error = "Username consist of digits, letters, dots or underscores"
                call.redirect(signUpError)
            }
            db.user(userId) != null -> {
                signUpError.error = "User with the following user name is already registered"
                call.redirect(signUpError)
            }
            else -> {
                val hash = hashFunction(password)
                val newUser = User(userId, email, displayName, hash)

                try {
                    db.createUser(newUser)
                } catch (e: Throwable) {
                    when {
                        db.user(userId) != null -> {
                            signUpError.error = "User with the following user name is already registered"
                            call.redirect(signUpError)
                        }
                        db.userByEmail(email) != null -> {
                            signUpError.error = "User with the following $email is already registered"
                            call.redirect(signUpError)
                        }
                        else -> {
                            val errorMsg = "failed to register user"
                            application.log.error(errorMsg, e)
                            signUpError.error = errorMsg
                            call.redirect(signUpError)
                        }
                    }

                    call.sessions.set(EPSession(newUser.userId))
                    call.redirect(Phrases())
                }
            }
        }

    }

    get<SignUp> {
        val user = call.sessions.get<EPSession>()?.let { db.user(it.userId) }
        if (user != null) {
            call.redirect(Phrases())
        } else {
            call.redirect(
                FreeMarkerContent("signin.ftl", mapOf("error" to it.error))
            )
        }
    }
}