package gay.vaskel.bot

import gay.vaskel.bot.handlers.PrefixProvider
import gay.vaskel.bot.utils.Configuration
import me.devoxin.flight.api.events.*
import me.devoxin.flight.internal.utils.Flight
import me.devoxin.flight.internal.utils.on
import mixtape.commons.extensions.JDA
import mixtape.commons.extensions.intents
import mu.KotlinLogging
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
        intents += GatewayIntent.GUILD_MEMBERS
        setMemberCachePolicy(MemberCachePolicy.ALL)
    }

    /*val app = Javalin.create().start(Configuration.config.interop.port)
    app.get("/members/count") { ctx ->
        ctx.result("${JDA.userCache.count()}")
    }*/
}