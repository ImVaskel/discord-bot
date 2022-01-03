package gay.vaskel.bot.cogs

import gay.vaskel.bot.handlers.PrefixProvider
import gay.vaskel.bot.utils.Configuration
import gay.vaskel.common.models.tables.ConfigTable
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.annotations.Name
import me.devoxin.flight.api.annotations.SubCommand
import me.devoxin.flight.api.entities.Cog
import mixtape.commons.extensions.reply
import net.dv8tion.jda.api.Permission
import org.jetbrains.exposed.sql.transactions.transaction
import pw.forst.exposed.insertOrUpdate

class Prefix: Cog {
    @Command(
        description = "The group for changing the prefix, if there is no subcommand passed, it will instead return the current prefix."
    )
    @Name("prefix")
    fun prefixBaseCommand(ctx: Context) {
        val configuration =
            (ctx.flight.resources.prefixProvider as PrefixProvider).configurations[ctx.guild!!.idLong] ?: return

        ctx.reply {
            description = "The prefix for this guild is ``${configuration.prefix}``."
        }
    }

    @SubCommand(userPermissions = [Permission.MANAGE_SERVER])
    @Name("set")
    fun prefixSetCommand(ctx: Context, prefix: String) {
        if (prefix.length > 20) {
            ctx.reply {
                description = "Sorry, the max length for prefixes is 20 characters."
            }
            return
        }

        transaction {
            ConfigTable.insertOrUpdate(ConfigTable.id) {
                it[this.id] = ctx.guild!!.idLong
                it[this.prefix] = prefix
            }
        }

        (ctx.flight.resources.prefixProvider as PrefixProvider).updateMappingFor(ctx.guild!!.idLong)

        ctx.reply {
            description = "Successfully changed the prefix for the guild."
        }

    }

    @SubCommand(userPermissions = [Permission.MANAGE_SERVER])
    @Name("remove")
    fun prefixRemoveCommand(ctx: Context) {
        transaction {
            ConfigTable.insertOrUpdate(ConfigTable.id) {
                it[id] = ctx.guild!!.idLong
                it[prefix] = Configuration.config.discord.prefix
            }
        }
        (ctx.flight.resources.prefixProvider as PrefixProvider).updateMappingFor(ctx.guild!!.idLong)

        ctx.reply {
            description = "Changed the prefix for this guild to the default, which is ``${Configuration.config.discord.prefix}``."
        }
    }
}