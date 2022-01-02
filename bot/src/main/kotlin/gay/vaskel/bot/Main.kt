package gay.vaskel.bot

import gay.vaskel.bot.utils.Configuration
import gay.vaskel.bot.utils.JDABuilder
import me.devoxin.flight.internal.utils.Flight
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

    val flight = Flight {
        prefixes += Configuration.config.discord.prefix
        developers = Configuration.config.discord.owner_ids.toMutableSet()
        registerDefaultResolvers()
    }
    flight.commands.register("gay.vaskel.bot.cogs")

    val JDA = JDABuilder(Configuration.config.discord.token) {
        addEventListeners(flight)
    }
}