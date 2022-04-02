package gay.vaskel.backend.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val CDN_BASE_URL = "https://cdn.discordapp.com"

@Serializable
data class User(
    val id: String,
    val username: String,
    val discriminator: String,
    @SerialName("avatar")
    val avatarHash: String?
) {
    @SerialName("avatar_url")
    val avatarUrl: String =
            if (avatarHash != null) "$CDN_BASE_URL/$avatarHash"
            else "$CDN_BASE_URL/embed/avatars/${discriminator.toInt() % 5}"
}