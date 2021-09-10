package com.example.project_health.model

class APIResponse {
    var error: Boolean = false
    var uId: Int? = null
    var errorMsg: String? = null
    var user: UserModel? = null
    var bioStats: BioStatsModel? = null
    var params: ParamsModel? = null
    val friendParams = mutableListOf<NameStepsModel>()
    val friendsList = mutableListOf<UserModel>()
}

