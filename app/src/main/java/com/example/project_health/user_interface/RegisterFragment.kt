package com.example.project_health.user_interface

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.project_health.R
import com.example.project_health.common.Common
import com.example.project_health.model.APIResponse
import com.example.project_health.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment(R.layout.register_fragment) {

    private lateinit var mService: IMyAPI
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var userNameField: EditText
    private lateinit var repeatPasswordField: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val registerButton = view.findViewById<TextView>(R.id.registerButton)

        mService = Common.api

        registerButton.setOnClickListener {

            userNameField = view.findViewById<EditText>(R.id.userInput)
            passwordField = view.findViewById<EditText>(R.id.passwordInput)
            repeatPasswordField = view.findViewById<EditText>(R.id.repeatPasswordInput)
            emailField = view.findViewById<EditText>(R.id.emailInput)

            insertUser(userNameField.text.toString(), emailField.text.toString(),
                passwordField.text.toString(), repeatPasswordField.text.toString())
        }

        val backButton = view.findViewById<TextView>(R.id.backButton)
        backButton.setOnClickListener {
            val loginFragment = LoginFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_view, loginFragment)
            transaction.commit()
        }
    }

    private fun insertUser(userName: String,userEmail: String,userPassword: String, userPasswordRepeat: String) {

        mService.registerUser(userName, userEmail, userPassword, userPasswordRepeat).enqueue(object: Callback<APIResponse> {

            override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                Toast.makeText(context as LoginActivity, t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context as LoginActivity, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                    passwordField.setText("")
                    repeatPasswordField.setText("")

                } else {
                    Toast.makeText(context as LoginActivity, "Account Registered", Toast.LENGTH_SHORT).show()
                    val loginFragment = LoginFragment()
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.fragment_view, loginFragment)
                    transaction.commit()
                }
            }
        })
    }
}

