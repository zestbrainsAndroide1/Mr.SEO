package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class ApplyModel {
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

    class Data : Serializable{
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("threads_id")
        @Expose
        var threadsId: Int? = null

        @SerializedName("sender_id")
        @Expose
        var senderId: Int? = null

        @SerializedName("receiver_id")
        @Expose
        var receiverId: Int? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("file")
        @Expose
        var file: String? = null

        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("sender_status")
        @Expose
        var senderStatus: String? = null

        @SerializedName("receiver_status")
        @Expose
        var receiverStatus: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("sender_img")
        @Expose
        var senderImg: String? = null

        @SerializedName("sender_name")
        @Expose
        var senderName: String? = null

        @SerializedName("receiver_img")
        @Expose
        var receiverImg: String? = null

        @SerializedName("receiver_name")
        @Expose
        var receiverName: String? = null
    }
}