package com.example.project_health.remote

import com.example.project_health.model.APIResponse
import com.example.project_health.model.UserModel
import retrofit2.Call
import retrofit2.http.*
import java.sql.Date

interface IMyAPI {

    @FormUrlEncoded
    @POST("login.php")
    fun loginUser(
        @Field("userEmail") userEmail: String,
        @Field("userPassword") userPassword: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("registerUser.php")
    fun registerUser(
        @Field("userName") userName: String,
        @Field("userEmail") userEmail: String,
        @Field("userPassword") userPassword: String,
        @Field("userPasswordRepeat") userPasswordRepeat: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("registerUserParams.php")
    fun registerUserParams(
        @Field("userDateBirth") userDateBirth: String,
        @Field("userSex") userSex: String,
        @Field("userId") userId:Int
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("registerBioStats.php")
    fun registerBioStats(
        @Field("userHeight") userHeight: String,
        @Field("userWeight") userWeight: String,
        @Field("dataDate") currentDate: String,
        @Field("userId") userId: Int
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("registerFriendship.php")
    fun registerFriendship(
        @Field("userId") userId: Int,
        @Field("friendEmail") friendEmail: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("searchFriends.php")
    fun searchFriends(
        @Field("userId") userId: Int
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("deleteFriend.php")
    fun deleteFriend(
        @Field("userId") userId: Int,
        @Field("friendEmail") friendEmail:String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("searchCompetitionInfo.php")
    fun searchCompetitionInfo(
        @Field("userEmail") userEmail: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("getUserDevParams.php")
    fun getUserDevParams(
        @Field("userEmail") userEmail: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("updateUserName.php")
    fun updateUserName(
        @Field("userEmail") userOldEmail: String,
        @Field("userName") userName: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("updatePassword.php")
    fun updateUserPassword(
        @Field("userEmail") userEmail: String,
        @Field("newPassword") newPassword: String,
        @Field("newPassRepeat") newPassRepeat: String,
        @Field("oldPassword") oldPassword: String
    ): Call<APIResponse>

    @FormUrlEncoded
    @POST("getUserOverallData.php")
    fun getUserBioInfo(
        @Field("userEmail") userEmail: String
    ): Call<APIResponse>
}
