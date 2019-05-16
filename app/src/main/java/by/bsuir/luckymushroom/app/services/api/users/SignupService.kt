package by.bsuir.luckymushroom.app.services.api.users

import by.bsuir.luckymushroom.app.dto.users.User
import by.bsuir.luckymushroom.app.dto.users.UserCredentials
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignupService {
    @POST("/api/users/signup")
    fun createUser(@Body userCred: UserCredentials): Deferred<Response<User>>
}