package gay.vaskel.bot.handlers

import gay.vaskel.bot.utils.Configuration
import gay.vaskel.common.models.GuildConfiguration
import gay.vaskel.common.models.tables.ConfigTable
import me.devoxin.flight.api.entities.PrefixProvider
import net.dv8tion.jda.api.entities.Message
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PrefixProvider: PrefixProvider {
    val configurations = mutableMapOf<Long, GuildConfiguration>()

    init {
        transaction {
            ConfigTable.selectAll().forEach {
                configurations[it[ConfigTable.id]] = GuildConfiguration(it)
            }
        }
    }

    fun updateMappingFor(guild: Long) {
        val row = transaction {
            ConfigTable.select {
                ConfigTable.id eq guild
            }.firstOrNull()
        } ?: return

        configurations[guild] = GuildConfiguration(row)
    }

    override suspend fun provide(message: Message): List<String> {
        val me = message.jda.selfUser.id
        val prefixes = mutableListOf("<@$me>", "<@!$me>")

        prefixes += if(message.isFromGuild) {
            configurations[message.guild.idLong]?.prefix ?: Configuration.config.discord.prefix
        }
        else {
            Configuration.config.discord.prefix
        }

        return prefixes
    }
}