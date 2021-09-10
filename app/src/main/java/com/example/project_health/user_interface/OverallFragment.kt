package com.example.project_health.user_interface

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_health.R
import com.example.project_health.common.Common
import com.example.project_health.model.APIResponse
import com.example.project_health.model.UserModel
import com.example.project_health.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OverallFragment : Fragment() {

    lateinit var overallTextView: TextView
    lateinit var stepsTextView: TextView
    lateinit var bpmTextView: TextView
    lateinit var satTextView: TextView
    lateinit var sweatTextView: TextView
    lateinit var sleepTextView: TextView
    lateinit var user: UserModel
    lateinit var mService: IMyAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.overall_fragment, container, false)

        val bundle = this.arguments
        user = bundle?.getParcelable("UserAccount")!!
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        overallTextView = view.findViewById(R.id.overallTextView)
        stepsTextView = view.findViewById(R.id.stepTextView)
        bpmTextView = view.findViewById(R.id.bpmTextView)
        satTextView = view.findViewById(R.id.satTextView)
        sweatTextView = view.findViewById(R.id.sweatTextView)
        sleepTextView = view.findViewById(R.id.sleepTextView)

        mService = Common.api

        loadBioStatsData(user.userEmail)
    }

    private fun loadBioStatsData(userEmail: String) {

        mService.getUserBioInfo(userEmail).enqueue(object: Callback<APIResponse>{

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context as MainActivity, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                } else {

                    overallTextView.text = "Height: ${response.body()!!.bioStats?.userHeight} cm" +
                            "\nWeight: ${response.body()!!.bioStats?.userWeight} Kg" +
                            "\nBiological sex: ${response.body()!!.bioStats?.userSex}"
                    stepsTextView.text = "Steps: ${response.body()!!.params?.steps}"
                    bpmTextView.text = "BPM (Avg): ${response.body()!!.params?.bpm}"
                    satTextView.text = "O2 Saturation (Avg): ${response.body()!!.params?.saturation}%"
                    sweatTextView.text = "Sweat L/h (Avg): ${response.body()!!.params?.sweatAmount}"
                    sleepTextView.text = "Sleep time: ${response.body()!!.params?.sleepHours} hours"
                    Toast.makeText(context as MainActivity, "Data loaded correctly", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(context as MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}