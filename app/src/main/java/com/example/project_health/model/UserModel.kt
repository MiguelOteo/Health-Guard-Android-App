package com.example.project_health.model

import android.os.Parcel
import android.os.Parcelable
import java.sql.Date

class UserModel() : Parcelable{
    var userId: Int = 0
    var userName: String = ""
    var userEmail: String = ""
    var userDateBirth: String? = null
    var userSex: String? = null

    constructor(parcel: Parcel) : this() {
        userId = parcel.readInt()
        userName = parcel.readString().toString()
        userEmail = parcel.readString().toString()
        userSex = parcel.readString().toString()
        userDateBirth = parcel.readString().toString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeString(userName)
        parcel.writeString(userEmail)
        parcel.writeString(userSex)
        parcel.writeString(userDateBirth)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}