package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class StatusModel {
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
        @SerializedName("help_id")
        @Expose
        var helpId: Int? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("post_id")
        @Expose
        var postId: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null
    }

}