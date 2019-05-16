package by.bsuir.luckymushroom.app.services.api.users

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST

interface LogoutService {
    @POST("/api/users/logout")
    fun logout(@Header("Cookie") cookie: String): Deferred<Response<Unit>>
}