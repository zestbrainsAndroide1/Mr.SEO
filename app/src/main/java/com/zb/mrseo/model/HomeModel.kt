package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class HomeModel {
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
    var data: ArrayList<Data>? = null

    class Data {


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

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("mall_name")
        @Expose
        var mallName: String? = null


        @SerializedName("blog_name")
        @Expose
        var blogName: String? = null

        @SerializedName("cafe_name")
        @Expose
        var cafeName: String? = null

        @SerializedName("register_point")
        @Expose
        var registerPoint: Int? = null

        @SerializedName("helper_count")
        @Expose
        var helperCount: String? = null

        @SerializedName("helper_user")
        @Expose
        var helperUser: String? = null

        @SerializedName("advertisements")
        @Expose
        var advertisements: Advertisement? = null

        class Advertisement {

            @SerializedName("image")
            @Expose
            var image: String? = null

            @SerializedName("url")
            @Expose
            var url: String? = null
        }


    }
}