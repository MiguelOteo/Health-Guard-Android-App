package com.example.project_health.user_interface

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_health.R
import com.example.project_health.common.Common
import com.example.project_health.model.APIResponse
import com.example.project_health.model.NameStepsModel
import com.example.project_health.model.UserModel
import com.example.project_health.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompetitionFragment: Fragment(R.layout.fragment_competition) {

    private lateinit var mService: IMyAPI
    private lateinit var user: UserModel
    private lateinit var userNameView: TextView
    private lateinit var userStepsView: TextView
    private lateinit var userPositionView: TextView

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCompetition.ViewHolder>? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = this.arguments
        user = bundle?.getParcelable("UserAccount")!!
        return inflater.inflate(R.layout.fragment_competition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mService = Common.api
        userNameView = view.findViewById(R.id.userNameView)
        userStepsView = view.findViewById(R.id.userStepsView)
        userPositionView = view.findViewById(R.id.userPositionView)

        loadCompetitionList(user.userEmail)
    }

    private fun loadCompetitionList(userEmail: String) {

        mService.searchCompetitionInfo(userEmail).enqueue(object: Callback<APIResponse> {
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context as MainActivity, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                } else {

                    var paramsList = response.body()!!.friendParams
                    getUserParams(user.userEmail, paramsList)
                    Toast.makeText(context as MainActivity, "Competition loaded", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(context as MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUserParams(userEmail: String, nameStepsList: MutableList<NameStepsModel>) {

        mService.getUserDevParams(userEmail).enqueue(object: Callback<APIResponse>{

            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context as MainActivity, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                } else {
                    var userNameSteps = NameStepsModel()
                    userNameSteps.friendName = user.userName
                    userNameSteps.steps = response.body()!!.params?.steps!!

                    nameStepsList.add(userNameSteps)
                    nameStepsList.sortByDescending { it.steps }

                    var count = 1
                    for(element in nameStepsList) {
                        if(element.joinId == null) {
                            userPositionView.text = "${count}."
                            break
                        } else {
                            count++
                        }
                    }
                    userNameView.text = user.userName
                    userStepsView.text = "Steps: ${response.body()!!.params?.steps!!}"

                    layoutManager = LinearLayoutManager(context as MainActivity)
                    var competitionListView = view?.findViewById<RecyclerView>(R.id.competitionListView)
                    competitionListView?.layoutManager = layoutManager

                    adapter = RecyclerAdapterCompetition()
                    (adapter as RecyclerAdapterCompetition).setData(nameStepsList)
                    competitionListView?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(context as MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}