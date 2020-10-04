package com.lastreact.repository

import com.lastreact.model.*
import com.zaxxer.hikari.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*

object DBFactory {

    init {
        Database.connect(hikari())

        transaction {
            SchemaUtils.create(EmojiPhrases)

            EmojiPhrases.insert {
                it[emoji] = "e1"
                it[phrase] = "p1"
            }
            EmojiPhrases.insert {
                it[emoji] = "e2"
                it[phrase] = "p2"
            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            jdbcUrl = "jdbc:h2:mem:test"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }.also {
            it.validate()
        }
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }
}