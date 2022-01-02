package gay.vaskel.bot.utils

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder

inline fun JDABuilder(token: String, jdaBuilder: JDABuilder.() -> Unit): JDA {
    return JDABuilder.createDefault(token).apply(jdaBuilder).build()
}