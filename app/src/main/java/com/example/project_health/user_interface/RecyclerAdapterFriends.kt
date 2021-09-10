package com.example.project_health.user_interface

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project_health.R
import com.example.project_health.common.Common
import com.example.project_health.model.APIResponse
import com.example.project_health.model.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerAdapterFriends: RecyclerView.Adapter<RecyclerAdapterFriends.ViewHolder>() {

    private lateinit var friendsNames: MutableList<String>
    private lateinit var friendsEmail: MutableList<String>
    private var userId: Int = 0
    private lateinit var context: Context
    private var mService = Common.api

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterFriends.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.card_friend, parent, false)
        context = parent.context
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: RecyclerAdapterFriends.ViewHolder, position: Int) {
        holder.userNameView.text = friendsNames[position]
    }

    override fun getItemCount(): Int {
       return friendsNames.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var userNameView: TextView = itemView.findViewById(R.id.userNameView)
        private var userDeleteButton: ImageView = itemView.findViewById(R.id.deleteFriend)

        init {
            userDeleteButton.setOnClickListener {
                val positionArray: Int = bindingAdapterPosition
                deleteFriendFun(userId, friendsEmail[positionArray], bindingAdapterPosition)
            }
        }
    }

    fun setData(friendList: MutableList<UserModel>, userId: Int) {
        friendsNames = friendList.map { it.userName } as MutableList<String>
        friendsEmail = friendList.map { it.userEmail } as MutableList<String>
        this.userId = userId
    }

    fun deleteFriendFun(userId: Int, friendEmail: String, bindingAdapterPosition: Int) {

        mService.deleteFriend(userId, friendEmail).enqueue(object: Callback<APIResponse> {

            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Friend deleted successfully", Toast.LENGTH_SHORT).show()
                    friendsEmail.removeAt(bindingAdapterPosition)
                    friendsNames.removeAt(bindingAdapterPosition)
                    notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

