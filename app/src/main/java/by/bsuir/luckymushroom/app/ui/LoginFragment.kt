package by.bsuir.luckymushroom.app.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.App
import by.bsuir.luckymushroom.app.dto.users.User
import by.bsuir.luckymushroom.app.dto.users.UserCredentials
import com.google.common.hash.Hashing
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.charset.StandardCharsets

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LoginFragment : Fragment() {

    interface OnClickListener {
        fun runMain()
    }

    lateinit var onClickListener: OnClickListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onClickListener = activity as OnClickListener

        return inflater.inflate(R.layout.fragment_login, container, false)
            .apply {
                val editTextPassword =
                    findViewById<EditText>(R.id.editTextPassword)
                val editTextMail = findViewById<EditText>(R.id.editTextMail)
                findViewById<Button>(R.id.buttonSingUp).apply {
                    setOnClickListener {
                        singUp(
                            editTextMail.text.toString(),
                            editTextPassword.text.toString()
                        )
                    }
                }
                findViewById<Button>(R.id.buttonLogIn).apply {
                    setOnClickListener {
                        logIn(
                            editTextMail.text.toString(),
                            editTextPassword.text.toString()
                        )
                    }
                }
                findViewById<Button>(R.id.buttonSkip).apply {
                    setOnClickListener {
                        onClickListener.runMain()
                    }
                }
            }
    }

    fun logIn(mail: String, password: String) {
        val passwordHash = Hashing.sha512().hashString(
            password,
            StandardCharsets.UTF_8
        ).toString()

        App.loginService.getUser(
            UserCredentials(mail, passwordHash)
        ).enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if (response.isSuccessful) {
                    App.user = response.body()
                    App.cookie =
                        response.headers().get("Set-Cookie")
                            ?: ""
                    onClickListener.runMain()
                } else {
                    Toast.makeText(
                        activity, "Cannot log in",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<User>,
                t: Throwable
            ) {
                Toast.makeText(
                    activity, "Cannot log in",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }

    fun singUp(mail: String, password: String) {
        val passwordHash = Hashing.sha512().hashString(
            password,
            StandardCharsets.UTF_8
        ).toString()

        App.signupService.createUser(
            UserCredentials(mail, passwordHash)
        ).enqueue(object : Callback<User> {
            override fun onResponse(
                call: Call<User>,
                response: Response<User>
            ) {
                if (response.isSuccessful) {
                    App.user = response.body()
                    App.cookie =
                        response.headers().get("Set-Cookie")
                            ?: ""
                    onClickListener.runMain()
                } else {
                    Toast.makeText(
                        activity, "Cannot sign up",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<User>,
                t: Throwable
            ) {
                Toast.makeText(
                    activity, "Cannot sign up",
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }

}
