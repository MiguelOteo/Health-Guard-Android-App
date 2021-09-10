package com.example.project_health.common

import com.example.project_health.remote.IMyAPI
import com.example.project_health.remote.RetroClient

object Common {

    //In emulator "10.0.2.2" for the "localhost"
    private const val BASE_URL = "http://192.168.1.44/DataBasePHP/"

    val api: IMyAPI
    get() = RetroClient.getClient(BASE_URL).create(IMyAPI:: class.java)
}