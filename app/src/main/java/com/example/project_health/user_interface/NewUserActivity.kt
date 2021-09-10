 package com.example.project_health.user_interface

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.project_health.R
import com.example.project_health.common.Common
import com.example.project_health.model.APIResponse
import com.example.project_health.model.UserModel
import com.example.project_health.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class NewUserActivity : AppCompatActivity() {

    private lateinit var mService: IMyAPI
    private lateinit var userDateBirthField: TextView
    private lateinit var userSexInput: EditText
    private lateinit var userWeightInput: EditText
    private lateinit var userHeightInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_user_activity)

        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.orange_main);

        mService = Common.api

        userDateBirthField = findViewById(R.id.userDateBirthInput)
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateField(myCalendar)
        }

        userDateBirthField.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this, R.style.datePicker ,datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.setOnShowListener {
                val positiveColor = ContextCompat.getColor(this, R.color.orange_secondary)
                val negativeColor = ContextCompat.getColor(this, R.color.orange_secondary)
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(positiveColor)
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(negativeColor)
            }
            datePickerDialog.show()
        }

        val continueButton = findViewById<RelativeLayout>(R.id.continueButton)
        continueButton.setOnClickListener {

            userSexInput = findViewById<EditText>(R.id.userSexInput)
            userWeightInput = findViewById<EditText>(R.id.userWeightInput)
            userHeightInput = findViewById<EditText>(R.id.userHeightInput)

            val user = this.intent.getParcelableExtra<UserModel>("UserObject")

            insertUserPrams(this, userHeightInput.text.toString(), userWeightInput.text.toString(), userSexInput.text.toString()
                , userDateBirthField.text.toString(), user?.userId!!)
        }
    }

    private fun updateDateField(myCalendar: Calendar) {
        val format = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(format, Locale.UK)
        userDateBirthField.text = sdf.format(myCalendar.time)
    }

    private fun insertUserPrams(context: Context, userHeight: String, userWeight: String, userSex: String, userDateBirth: String, userId: Int) {

        mService.registerUserParams(userDateBirth, userSex, userId).enqueue(object: Callback<APIResponse> {

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "User data uploaded correctly", Toast.LENGTH_SHORT).show()
                    insertBioStats(context, userHeight, userWeight, userId)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("UserObject", response.body()!!.user)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertBioStats(context: Context, userHeight: String, userWeight: String, userId: Int) {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDate = current.format(formatter)

        mService.registerBioStats(userHeight, userWeight, currentDate, userId).enqueue(object: Callback<APIResponse> {

            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Health data uploaded correctly", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}