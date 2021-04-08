package com.zb.mrseo.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class NoticeModel {
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
    var data: ArrayList<Datum>? = null

    class Datum {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("image")
        @Expose
        var image: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null
    }
}