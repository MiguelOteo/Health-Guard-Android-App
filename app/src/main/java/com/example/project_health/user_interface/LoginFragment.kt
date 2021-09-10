package com.example.project_health.user_interface

import android.content.Intent
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
import com.example.project_health.model.UserModel
import com.example.project_health.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private lateinit var mService: IMyAPI
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mService = Common.api

        val registerButton = view.findViewById<TextView>(R.id.RegisterButton)
        registerButton.setOnClickListener {

            val registerFragment = RegisterFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_view, registerFragment)
            transaction.commit()
        }

        val loginButton = view.findViewById<TextView>(R.id.LoginButton)
        loginButton.setOnClickListener {

            emailField = view.findViewById(R.id.emailInput)
            passwordField = view.findViewById(R.id.passwordInput)
            authenticateUser(emailField.text.toString(), passwordField.text.toString())
        }
    }

    private fun authenticateUser(userEmail: String, userPassword: String) {

        mService.loginUser(userEmail, userPassword).enqueue(object: Callback<APIResponse> {

            override fun onFailure(call: Call<APIResponse>?, t: Throwable?) {
                Toast.makeText(context as LoginActivity, t!!.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context as LoginActivity, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                    emailField.setText("")
                    passwordField.setText("")

                } else {
                    Toast.makeText(context as LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                    val userObject: UserModel? = response.body()!!.user
                    if(userObject?.userDateBirth == null && userObject?.userSex == null) {

                        val intent = Intent(activity, NewUserActivity::class.java)
                        intent.putExtra("UserObject", userObject)
                        activity?.startActivity(intent)
                        emailField.setText("")
                        passwordField.setText("")
                    } else {

                        val intent = Intent(activity, MainActivity::class.java)
                        intent.putExtra("UserObject", userObject)
                        activity?.startActivity(intent)
                        emailField.setText("")
                        passwordField.setText("")
                    }
                }
            }
        })
    }
}
