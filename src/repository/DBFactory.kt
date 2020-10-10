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
            SchemaUtils.create(Users)
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = System.getenv("JDBC_DATABASE_URL")
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