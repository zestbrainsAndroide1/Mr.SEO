package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PostModel {
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
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("thread_id")
        @Expose
        var threadsId: Int? = null

        @SerializedName("user_name")
        @Expose
        var userName: String? = null

        @SerializedName("category_id")
        @Expose
        var categoryId: String? = null

        @SerializedName("category_name")
        @Expose
        var categoryName: String? = null

        @SerializedName("platform_id")
        @Expose
        var platformId: Int? = null

        @SerializedName("platform_name")
        @Expose
        var platformName: String? = null

        @SerializedName("instruction_msg")
        @Expose
        var instructionMsg: String? = null


        @SerializedName("helper_count")
        @Expose
        var helperCount: String? = null


        @SerializedName("platform_info")
        @Expose
        var platformInfo: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("point")
        @Expose
        var point: String? = null

        @SerializedName("helper_user")
        @Expose
        var helperUser: String? = null

        @SerializedName("url")
        @Expose
        var url: String? = null

        @SerializedName("keyword")
        @Expose
        var keyword: String? = null

        @SerializedName("title")
        @Expose
        var title: String? = null
    }
}