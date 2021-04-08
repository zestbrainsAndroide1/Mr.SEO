package com.zb.mrseo.restapi

import com.google.gson.annotations.SerializedName


class ApiStringError {

    @SerializedName("status")
    var status: Int? = null

    @SerializedName("error")
    var error: String? = null

    @SerializedName("success")
    var success: String? = null

    @SerializedName("username")
    var username: String? = null

    @SerializedName("ic_email")
    var email: String? = null

    @SerializedName("mobile")
    var mobile: String? = null

    @SerializedName("message")
    var message: String? = null

}
