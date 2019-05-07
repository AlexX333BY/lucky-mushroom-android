package by.bsuir.luckymushroom.app.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.App
import by.bsuir.luckymushroom.app.dto.users.User
import by.bsuir.luckymushroom.app.dto.users.UserCredentials
import com.google.common.hash.Hashing
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.StandardCharsets

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // TODO: add validatorsz
        buttonLogIn.setOnClickListener {

            val passwordHash = Hashing.sha512()
                .hashString(
                    editTextPassword.text.toString(), StandardCharsets.UTF_8
                ).toString()
            val self = this
            val user = App.loginService.getUser(
                UserCredentials(
                    editTextMail.text.toString(), passwordHash
                )
            ).enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>?,
                    response: Response<User>
                ) {
                    if (response.isSuccessful) {
                        App.user = response.body()
                        App.cookie = response.headers().get("Set-Cookie") ?: ""
                        runMainActivity()
                    } else {
                        val toast = Toast.makeText(
                            self, "Cannot sign up", Toast.LENGTH_LONG
                        )
                        toast.show()
                    }

                }

                override fun onFailure(call: Call<User>?, t: Throwable) {
                    val toast = Toast.makeText(
                        self, "Cannot sign up", Toast.LENGTH_LONG
                    )
                    toast.show()
                }
            })
        }
        // TODO: add validators
        buttonSingUp.setOnClickListener {
            val passwordHash = Hashing.sha512()
                .hashString(
                    editTextPassword.text.toString(), StandardCharsets.UTF_8
                ).toString()
            val self = this
            App.signupService.createUser(
                UserCredentials(
                    editTextMail.text.toString(), passwordHash
                )
            )
                .enqueue(object : Callback<User> {
                    override fun onResponse(
                        call: Call<User>?,
                        response: Response<User>
                    ) {
                        if (response.isSuccessful) {
                            App.user = response.body()
                            App.cookie =
                                response.headers().get("Set-Cookie") ?: ""
                            runMainActivity()
                        } else {
                            val toast = Toast.makeText(
                                self, "Cannot log in", Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                    }

                    override fun onFailure(call: Call<User>?, t: Throwable) {
                        val toast = Toast.makeText(
                            self, "Cannot log in", Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                })
        }

        buttonSkip.setOnClickListener {
            runMainActivity()
        }

    }

    fun runMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}

