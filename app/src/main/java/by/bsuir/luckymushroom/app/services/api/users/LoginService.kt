package by.bsuir.luckymushroom.app.services.api.users

import by.bsuir.luckymushroom.app.dto.User
import by.bsuir.luckymushroom.app.dto.UserCredentials
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/api/users/login")
    fun getUser(@Body userCred: UserCredentials): Call<User>
}