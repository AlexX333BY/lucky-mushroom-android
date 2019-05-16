package by.bsuir.luckymushroom.app.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import by.bsuir.luckymushroom.app.App
import by.bsuir.luckymushroom.app.dto.users.User
import by.bsuir.luckymushroom.app.dto.users.UserCredentials
import com.google.common.hash.Hashing
import kotlinx.coroutines.*
import java.nio.charset.StandardCharsets

class UserViewModel : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val authError: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            it.value = ""
        }
    }
    private val user = MutableLiveData<User?>()

    fun getAuthError(): LiveData<String> {
        return authError
    }

    fun getUser(): LiveData<User?> {
        return user
    }

    fun launchSignUp(mail: String, password: String) {
        uiScope.launch {
            signUp(mail, password)
        }
    }

    fun launchLogIn(mail: String, password: String) {
        uiScope.launch {
            logIn(mail, password)
        }
    }

    suspend fun signUp(mail: String, password: String) =
        withContext(Dispatchers.IO) {
            val passwordHash = Hashing.sha512().hashString(
                password,
                StandardCharsets.UTF_8
            ).toString()
            val response =
                App.signupService.createUser(
                    UserCredentials(mail, passwordHash)
                )
                    .await()
            try {
                if (response.isSuccessful) {
                    user.postValue(response.body())
                    App.cookie = response.headers().get("Set-Cookie") ?: ""
                } else {
                    authError.postValue("cannot sign up")
                }
            } catch (e: Exception) {
                authError.postValue("cannot sign up")
            }
        }

    suspend fun logIn(mail: String, password: String) =
        withContext(Dispatchers.IO) {
            val passwordHash = Hashing.sha512().hashString(
                password,
                StandardCharsets.UTF_8
            ).toString()
            val response =
                App.loginService.getUser(UserCredentials(mail, passwordHash))
                    .await()
            try {
                if (response.isSuccessful) {
                    user.postValue(response.body())
                    App.cookie = response.headers().get("Set-Cookie") ?: ""

                } else {
                    authError.postValue("cannot log in")
                }
            } catch (e: Exception) {
                authError.postValue("cannot log in")
            }
        }

}