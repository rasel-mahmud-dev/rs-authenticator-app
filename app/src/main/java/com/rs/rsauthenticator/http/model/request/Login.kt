package com.rs.rsauthenticator.http.model.request


data class LoginApiResponse(
    val message: String?,
    val data: LoginData?
) {
    data class LoginData(
        val user: User,
        val session: Session,
    ) {
        data class User(
            val _id: String,
            val device: String,
            val username: String,
            val email: String,
            val password: String,
            val avatar: String,
            val createdAt: String,
            val status: String,
            val deviceId: String
        )


    }
}


class LoginApiRequest {

}