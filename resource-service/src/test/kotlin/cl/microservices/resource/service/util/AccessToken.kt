package cl.microservices.resource.service.util

data class AccessToken(
    var access_token: String,
    var expires_in: Int,
    var refresh_expires_in: Int,
    var refresh_token: String,
    var token_type: String,
    var notBeforePolicy: Int,
    var session_state: String,
    var scope: String
)
