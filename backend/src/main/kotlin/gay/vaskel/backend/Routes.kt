package gay.vaskel.backend

import gay.vaskel.backend.entities.*
import gay.vaskel.backend.extensions.http
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*

const val DISCORD_API_URL = "https://discord.com/api"

@OptIn(InternalAPI::class)
fun Application.routes() {
    install(Sessions) {
        cookie<UserSession>("user_session")
    }

    install(Authentication) {
        oauth("auth") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "discord",
                    authorizeUrl = "https://discord.com/api/oauth2/authorize",
                    accessTokenUrl = "https://discord.com/api/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = Configuration.config.discord.id.toString(),
                    clientSecret = Configuration.config.discord.secret,
                    defaultScopes = listOf("identify", "guilds")
                )
            }
            client = this@routes.http
        }
    }
    routing {
        authenticate("auth") {
            get("/login") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                call.sessions.set(UserSession(principal?.accessToken.toString()))
                call.respondRedirect("/")
            }
        }

        get("/logout") {
            if (call.sessions.get<UserSession>() != null) {
                call.sessions.clear<UserSession>()
                call.respond(AuthenticatedResponse(true, mapOf()))
            }
            else {
                call.response.status(HttpStatusCode.Unauthorized)
                call.respond(
                    ErrorResponse(ErrorResponseData("unauthorized"))
                )
            }
        }

        get("/authenticated") {
            val session = call.sessions.get<UserSession>()
            call.respond(
                AuthenticatedResponse(true, mapOf("authenticated" to (session != null)))
            )
        }

        get("/userinfo") {
            val session: UserSession? = call.sessions.get()
            if (session == null) {
                call.response.status(HttpStatusCode.Unauthorized)
                call.respond(
                    ErrorResponse(ErrorResponseData("unauthorized"))
                )
            }
            else {
                val userInfo: User = http.get("$DISCORD_API_URL/users/@me") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${session.token}")
                    }
                }
                call.respond(
                    UserinfoResponse(userInfo)
                )
            }
        }
    }
}

data class UserSession(val token: String)