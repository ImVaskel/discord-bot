package gay.vaskel.bot.cogs

import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.annotations.Name
import me.devoxin.flight.api.annotations.SubCommand
import me.devoxin.flight.api.entities.Cog
import net.dv8tion.jda.api.Permission

class Prefix: Cog {
    @Command(
        userPermissions = [Permission.MANAGE_SERVER]
    )
    fun prefixBaseCommand(ctx: Context) {

    }

    @SubCommand()
    @Name("set")
    fun prefixSetCommand(ctx: Context, prefix: String) {

    }

    @SubCommand()
    @Name("remove")
    fun prefixRemoveCommand(ctx: Context) {

    }
}