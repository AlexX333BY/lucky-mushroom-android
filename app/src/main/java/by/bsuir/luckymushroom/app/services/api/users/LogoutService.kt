package by.bsuir.luckymushroom.app.services.api.users

import by.bsuir.luckymushroom.app.App
import retrofit2.http.Header
import retrofit2.http.POST

interface LogoutService {
    @POST("/api/users/logout")
    fun logout(@Header("Cookie") cookie: String = App.cookie)
}