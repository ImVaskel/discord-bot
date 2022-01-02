package gay.vaskel.bot

import gay.vaskel.bot.utils.Configuration
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database

val logger = KotlinLogging.logger {}

fun main() {
    Database.connect(
        "jdbc:postgresql://localhost/{${Configuration.config.database.name}",
        "org.postgresql.Driver",
        Configuration.config.database.username,
        Configuration.config.database.password
    )
}