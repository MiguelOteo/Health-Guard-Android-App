package com.example.project_health.user_interface

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_health.R
import com.example.project_health.model.NameStepsModel
import com.example.project_health.model.ParamsModel
import com.example.project_health.model.UserModel

class RecyclerAdapterCompetition: RecyclerView.Adapter<RecyclerAdapterCompetition.ViewHolder>() {

    //private var friendsNames = arrayOf("Pepe", "Jose", "Bader", "Desert")
    //private var friendsSteps = arrayOf(323,21,3345,43244)

    private lateinit var friendsSteps: MutableList<Int>
    private lateinit var friendsNames: MutableList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterCompetition.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.card_competition, parent, false)
        return ViewHolder(layoutInflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCompetition.ViewHolder, position: Int) {
        holder.userNameView.text = friendsNames[position]
        holder.userStepsView.text = "Steps: ${friendsSteps[position]}"
        holder.userPositionView.text = "${position + 1}."
    }

    override fun getItemCount(): Int {
        return friendsNames.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var userNameView: TextView = itemView.findViewById(R.id.userNameView)
        var userStepsView: TextView = itemView.findViewById(R.id.userStepsView)
        var userPositionView: TextView = itemView.findViewById(R.id.userPositionView)
    }

    fun setData(friendNameSteps: MutableList<NameStepsModel>) {

        friendsNames = friendNameSteps.map { it.friendName } as MutableList<String>
        friendsSteps = friendNameSteps.map{ it.steps} as MutableList<Int>
    }
}