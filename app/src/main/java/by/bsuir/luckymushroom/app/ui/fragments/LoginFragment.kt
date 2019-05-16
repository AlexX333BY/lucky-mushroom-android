package by.bsuir.luckymushroom.app.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import by.bsuir.luckymushroom.R
import by.bsuir.luckymushroom.app.viewmodels.UserViewModel

class LoginFragment : Fragment() {
    interface OnClickListener {
        fun runMain()
    }

    lateinit var onClickListener: OnClickListener
    private lateinit var userModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userModel = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onClickListener = activity as OnClickListener

        return inflater.inflate(R.layout.fragment_login, container, false)
            .apply {
                activity?.let {
                    userModel.getAuthError().observe(it, Observer { authError ->
                        Toast.makeText(
                            it, authError,
                            Toast.LENGTH_LONG
                        ).show()
                    })
                }
                val editTextPassword =
                    findViewById<EditText>(R.id.editTextPassword)
                val editTextMail = findViewById<EditText>(R.id.editTextMail)
                findViewById<Button>(R.id.buttonSingUp).apply {
                    setOnClickListener {
                        userModel.launchSignUp(
                            editTextMail.text.toString(),
                            editTextPassword.text.toString()
                        )
                    }
                }
                findViewById<Button>(R.id.buttonLogIn).apply {
                    setOnClickListener {
                        userModel.launchLogIn(
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

}
