package com.example.project_health.user_interface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_health.R
import com.example.project_health.common.Common
import com.example.project_health.model.APIResponse
import com.example.project_health.model.UserModel
import com.example.project_health.remote.IMyAPI
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class SettingsFragment : Fragment(R.layout.settings_fragments) {

    private lateinit var mService: IMyAPI
    private lateinit var user: UserModel
    private lateinit var newPassRepeatInput: EditText
    private lateinit var newPassInput: EditText
    private lateinit var oldPassInput: EditText
    private lateinit var nameInput: EditText
    private lateinit var updateInfoButton: TextView
    private lateinit var changePassButton: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = this.arguments
        user = bundle?.getParcelable("UserAccount")!!
        return inflater.inflate(R.layout.settings_fragments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mService = Common.api

        nameInput = view.findViewById(R.id.nameInput)
        nameInput.hint = user.userName

        updateInfoButton = view.findViewById(R.id.updateInfoButton)
        updateInfoButton.setOnClickListener {
           changeUserName(user.userEmail, nameInput.text.toString())
        }

        newPassInput = view.findViewById(R.id.newPassInput)
        newPassRepeatInput = view.findViewById(R.id.newPassRepeatInput)
        oldPassInput = view.findViewById(R.id.oldPassInput)

        changePassButton = view.findViewById(R.id.changePassButton)
        changePassButton.setOnClickListener {

            changePassword(user.userEmail, newPassInput.text.toString(), newPassRepeatInput.text.toString(),
                oldPassInput.text.toString())
        }
    }

    private fun changeUserName(userEmail: String, userName: String) {

        mService.updateUserName(userEmail, userName).enqueue(object: retrofit2.Callback<APIResponse> {

            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {

                if(response.body()!!.error) {
                    Toast.makeText(context as MainActivity, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context as MainActivity, "Name update success", Toast.LENGTH_SHORT).show()
                    user = response.body()!!.user!!
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(context as MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun changePassword(userEmail: String, newPassword: String, newPassRepeat: String, oldPassword: String) {

        mService.updateUserPassword(userEmail, newPassword, newPassRepeat, oldPassword).enqueue(object: retrofit2.Callback<APIResponse>{

            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context as MainActivity, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context as MainActivity, "Password update success", Toast.LENGTH_SHORT).show()
                    user = response.body()!!.user!!
                    nameInput.hint = user.userName
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(context as MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}