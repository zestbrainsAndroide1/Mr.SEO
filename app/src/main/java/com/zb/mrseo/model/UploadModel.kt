package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UploadModel {
    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("coin")
    @Expose
    var coin: Int? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("help_id")
        @Expose
        var helpId: String? = null

        @SerializedName("file")
        @Expose
        var file: String? = null
    }
}