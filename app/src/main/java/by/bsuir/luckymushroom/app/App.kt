package by.bsuir.luckymushroom.app

import android.app.Application
import by.bsuir.luckymushroom.app.dto.User
import by.bsuir.luckymushroom.app.services.api.users.LoginService
import by.bsuir.luckymushroom.app.services.api.users.SignupService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {
        val BASE_URL = "http://192.168.0.106:58479"
        lateinit var loginService: LoginService
        lateinit var signupService: SignupService
        var user: User? = null
    }

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate() {
        super.onCreate()

        loginService = retrofit.create(LoginService::class.java)
        signupService = retrofit.create(SignupService::class.java)
    }


}