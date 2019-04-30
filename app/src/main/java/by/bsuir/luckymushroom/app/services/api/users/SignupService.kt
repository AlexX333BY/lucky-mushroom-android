package by.bsuir.luckymushroom.app.services.api.users

import by.bsuir.luckymushroom.app.dto.User
import by.bsuir.luckymushroom.app.dto.UserCredentials
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupService {
    @POST("/api/users/signup")
    fun createUser(@Body userCred: UserCredentials): Call<User>
}