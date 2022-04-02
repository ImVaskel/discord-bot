package gay.vaskel.backend.entities

import kotlinx.serialization.Serializable

// If possible, I would just make this where T is Serializable, but you cannot do that.
@Serializable
open class Response<T>(
    val ok: Boolean,
    val data: T
)

@Serializable
data class ErrorResponseData(val reason: String)

class ErrorResponse(data: ErrorResponseData): Response<ErrorResponseData>(false, data)

@Serializable
data class UserinfoResponseData(val user: User)

class UserinfoResponse(user: User): Response<UserinfoResponseData>(true, UserinfoResponseData(user))

@Serializable
data class AuthenticatedResponse(
    val ok: Boolean,
    val data: Map<String, Boolean>
)