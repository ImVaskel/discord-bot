package gay.vaskel.bot.utils

import com.sksamuel.hoplite.ConfigLoader

// File that handles the config of the discord bot
// This is mostly just an object class to load the config lazily and the dataclasses.

object Configuration {
    val config: Config by lazy {
        ConfigLoader().loadConfigOrThrow<Config>("/config.toml")
    }
}

data class Config(
    val discord: Discord,
    val database: Database
)

data class Discord(
    val token: String,
    val prefix: String,
    val owner_ids: List<Long>
)

data class Database(
    val name: String,
    val username: String,
    val password: String
)