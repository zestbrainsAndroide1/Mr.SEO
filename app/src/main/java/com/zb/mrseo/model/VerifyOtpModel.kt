package com.zb.mrseo.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class VerifyOtpModel {
    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Any>? = null
}