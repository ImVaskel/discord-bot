package gay.vaskel.bot.utils

import com.sksamuel.hoplite.ConfigLoader
import gay.vaskel.common.models.Config
import gay.vaskel.common.models.ExternalFilePropertySource

// File that handles the config of the discord bot
// This is mostly just an object class to load the config lazily and the dataclasses.

object Configuration {
    val config: Config by lazy {
        ConfigLoader {
            addSource(ExternalFilePropertySource("config.toml"))
        }.loadConfigOrThrow()
    }
}
