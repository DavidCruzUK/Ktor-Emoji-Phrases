package com.lastreact

import io.ktor.util.*
import javax.crypto.*
import javax.crypto.spec.*

const val MIN_USER_ID_LENGTH = 4
const val MIN_PASSWORD_LENGTH = 6

@KtorExperimentalAPI
val hasKey = hex(System.getenv("SECRET_KEY"))

@KtorExperimentalAPI
val hmacKey = SecretKeySpec(hasKey, "HmaSHA1")

@KtorExperimentalAPI
fun hash(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}
private val userIdPattern = "([A-Za-z0-9\\-_]+)".toRegex()

internal fun userNameValid(userId: String) = userId.matches(userIdPattern)