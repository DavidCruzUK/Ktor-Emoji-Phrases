package com.lastreact.repository

import com.lastreact.model.*
import java.lang.IllegalArgumentException
import java.util.concurrent.atomic.*

class InMemoryRepository : Repository {

    private val idCounter = AtomicInteger()
    private val phrases = ArrayList<EmojiPhrase>()

    override suspend fun add(phrase: EmojiPhrase): EmojiPhrase {
        if (phrases.contains(phrase)) {
            return phrases.find { it == phrase }!!
        }
        phrase.id = idCounter.incrementAndGet()
        phrases.add(phrase)
        return phrase
    }

    override suspend fun phrase(id: Int) = phrase(id.toString())

    override suspend fun phrase(id: String) = phrases.find { it.id.toString() == id } ?:
        throw IllegalArgumentException("No Phrase found for $id")

    override suspend fun phrases() = phrases.toList()

    override suspend fun remove(phrase: EmojiPhrase) {
        if (!phrases.contains(phrase)) {
            throw IllegalArgumentException("No Phrase found for ${phrase.id}")
        }
        phrases.remove(phrase)
    }

    override suspend fun remove(id: Int) = phrases.remove(phrase(id))

    override suspend fun remove(id: String) = phrases.remove(phrase(id))

    override suspend fun clear() = phrases.clear()

}