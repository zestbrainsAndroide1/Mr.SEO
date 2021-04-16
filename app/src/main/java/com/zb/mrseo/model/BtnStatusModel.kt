package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BtnStatusModel {
    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("coin")
    @Expose
    var coin: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("point_message")
        @Expose
        var pointMessage: String? = null

        @SerializedName("point_button")
        @Expose
        var pointButton: String? = null

        @SerializedName("ticket_message")
        @Expose
        var ticketMessage: String? = null

        @SerializedName("ticket_button")
        @Expose
        var ticketButton: String? = null
    }
}