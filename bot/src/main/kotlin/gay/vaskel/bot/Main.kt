package gay.vaskel.bot

import gay.vaskel.bot.handlers.PrefixProvider
import gay.vaskel.bot.utils.Configuration
import io.javalin.Javalin
import me.devoxin.flight.api.events.*
import me.devoxin.flight.internal.utils.Flight
import me.devoxin.flight.internal.utils.on
import mixtape.commons.extensions.JDA
import mixtape.commons.extensions.intents
import mu.KotlinLogging
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.jetbrains.exposed.sql.Database

val logger = KotlinLogging.logger {}

fun main() {
    Database.connect(
        "jdbc:postgresql://localhost/${Configuration.config.database.name}",
        "org.postgresql.Driver",
        Configuration.config.database.username,
        Configuration.config.database.password
    )

    val flight = Flight {
        prefixProvider = PrefixProvider()
        developers = Configuration.config.discord.owner_ids.toMutableSet()
        registerDefaultResolvers()
    }
    flight.on<ParsingErrorEvent> {
        error.printStackTrace()
    }
    flight.on<BadArgumentEvent> {
        println(this)
        error.printStackTrace()
    }
    flight.on<FlightExceptionEvent> {
        error.printStackTrace()
    }
    flight.on<CommandFailedEvent> {
        error.printStackTrace()
    }
    flight.on<BadEnvironmentEvent> {
        when (reason) {
            BadEnvironmentEvent.Reason.NonGuild -> ctx.send("This command can only be used in direct messages.")
            BadEnvironmentEvent.Reason.NonDeveloper -> ctx.send("This command is developer only.")
            BadEnvironmentEvent.Reason.NonNSFW -> ctx.send("This command is NSFW only.")
        }
    }
    flight.commands.register("gay.vaskel.bot.cogs")

    val JDA = JDA(Configuration.config.discord.token) {
        addEventListeners(flight)
        setMemberCachePolicy(MemberCachePolicy.ALL)
        enableIntents(GatewayIntent.getIntents(JDABuilder.GUILD_SUBSCRIPTIONS))
    }

    logger.info {
        JDA.gatewayIntents
    }

    val app = Javalin.create().start(Configuration.config.interop.port)
    app.get("/mutual/{user}") { ctx ->

        val id = ctx.pathParam("user")
        val guilds = mutableListOf<String>()

        for (guild in JDA.guildCache) {
            if (guild.getMemberById(id) != null) {
                guilds.add(guild.id)
            }
        }

        ctx.json(guilds)

    }
}