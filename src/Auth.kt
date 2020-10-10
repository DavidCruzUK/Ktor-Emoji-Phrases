package com.lastreact

import io.ktor.util.*
import javax.crypto.*
import javax.crypto.spec.*

@KtorExperimentalAPI
val hasKey = hex(System.getenv("SECRET_KEY"))

val hmacKey = SecretKeySpec(hasKey, "HmaSHA1")

@KtorExperimentalAPI
fun hash(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}
private val userIdPattern = "[a-zA-Z0-9\\.]".toRegex()

internal fun userNameValid(userId: String) = userId.matches(userIdPattern)