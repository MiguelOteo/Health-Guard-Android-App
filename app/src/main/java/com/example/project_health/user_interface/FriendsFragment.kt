package com.example.project_health.user_interface

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_health.R
import com.example.project_health.common.Common
import com.example.project_health.model.APIResponse
import com.example.project_health.model.UserModel
import com.example.project_health.remote.IMyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsFragment: Fragment()  {

    private lateinit var mService: IMyAPI
    private lateinit var addFriendInput: EditText
    private lateinit var addFriendButton: TextView
    private lateinit var user: UserModel

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterFriends.ViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = this.arguments
        user = bundle?.getParcelable("UserAccount")!!
        return inflater.inflate(R.layout.friends_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mService = Common.api

        loadFriendList(user.userId)

        addFriendInput = view.findViewById(R.id.newFriendInput)
        addFriendButton = view.findViewById(R.id.addFriendButton)
        addFriendButton.setOnClickListener {
            addFriend(user.userId, addFriendInput.text.toString())
        }
    }

    private fun addFriend(userId: Int, friendEmail: String) {

        mService.registerFriendship(userId, friendEmail).enqueue(object: Callback<APIResponse> {

            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context as MainActivity, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                    addFriendInput.setText("")
                } else {
                    Toast.makeText(context as MainActivity, "Friend added correctly", Toast.LENGTH_SHORT).show()
                    addFriendInput.setText("")
                    loadFriendList(user.userId)
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(context as MainActivity, t.message, Toast.LENGTH_SHORT).show()
                addFriendInput.setText("")
            }
        })
    }

    private fun loadFriendList(userId: Int) {

        mService.searchFriends(userId).enqueue(object: Callback<APIResponse>{

            override fun onResponse(call: Call<APIResponse>, response: Response<APIResponse>) {
                if(response.body()!!.error) {
                    Toast.makeText(context as MainActivity, response.body()!!.errorMsg, Toast.LENGTH_SHORT).show()
                } else {

                    layoutManager = LinearLayoutManager(context as MainActivity)
                    var friendListView = view?.findViewById<RecyclerView>(R.id.friendsListView)
                    friendListView?.layoutManager = layoutManager

                    adapter = RecyclerAdapterFriends()
                    (adapter as RecyclerAdapterFriends).setData(response.body()!!.friendsList, user.userId)
                    friendListView?.adapter = adapter
                }
            }

            override fun onFailure(call: Call<APIResponse>, t: Throwable) {
                Toast.makeText(context as MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}