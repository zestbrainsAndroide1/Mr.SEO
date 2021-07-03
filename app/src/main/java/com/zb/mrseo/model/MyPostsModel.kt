package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class MyPostsModel {
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
    var data: ArrayList<Datum>? = null

    class Datum {
        @SerializedName("post_id")
        @Expose
        var postId: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("category_id")
        @Expose
        var categoryId: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("color")
        @Expose
        var color: String? = null

        @SerializedName("keyword")
        @Expose
        var keyword: String? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("url")
        @Expose
        var url: String? = null

        @SerializedName("register_point")
        @Expose
        var registerPoint: Int? = null

        @SerializedName("helper_count")
        @Expose
        var helperCount: Int? = null

        @SerializedName("helper_user")
        @Expose
        var helperUser: Int? = null
    }
}