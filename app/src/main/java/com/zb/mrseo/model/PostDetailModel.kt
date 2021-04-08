package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class PostDetailModel {
    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    class Data {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("platform_id")
        @Expose
        var platformId: Int? = null

        @SerializedName("category_id")
        @Expose
        var categoryId: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @SerializedName("image")
        @Expose
        var image: String? = null

        @SerializedName("mall_name")
        @Expose
        var mallName: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("price")
        @Expose
        var price: String? = null

        @SerializedName("register_point")
        @Expose
        var registerPoint: Int? = null

        @SerializedName("is_active")
        @Expose
        var isActive: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("category")
        @Expose
        var category: ArrayList<Category>? = null

        @SerializedName("user")
        @Expose
        var user: ArrayList<User>? = null

        @SerializedName("platform")
        @Expose
        var platform: ArrayList<Platform>? = null

        class Platform {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("title")
            @Expose
            var title: String? = null

            @SerializedName("is_active")
            @Expose
            var isActive: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: Any? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: Any? = null
        }

        class Category {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("title")
            @Expose
            var title: String? = null

            @SerializedName("image")
            @Expose
            var image: String? = null

            @SerializedName("is_active")
            @Expose
            var isActive: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: Any? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: Any? = null

            @SerializedName("color")
            @Expose
            var color: String? = null
        }

        class User {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("nick_name")
            @Expose
            var nickName: String? = null

            @SerializedName("country_code")
            @Expose
            var countryCode: String? = null

            @SerializedName("mobile")
            @Expose
            var mobile: String? = null

            @SerializedName("profile_image")
            @Expose
            var profileImage: String? = null

            @SerializedName("email")
            @Expose
            var email: String? = null

            @SerializedName("type")
            @Expose
            var type: String? = null

            @SerializedName("is_active")
            @Expose
            var isActive: String? = null

            @SerializedName("bank_name")
            @Expose
            var bankName: String? = null

            @SerializedName("bank_image")
            @Expose
            var bankImage: String? = null

            @SerializedName("account_number")
            @Expose
            var accountNumber: String? = null

            @SerializedName("info")
            @Expose
            var info: String? = null

            @SerializedName("reset_token")
            @Expose
            var resetToken: Any? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null

            @SerializedName("updated_at")
            @Expose
            var updatedAt: String? = null

            @SerializedName("deleted_at")
            @Expose
            var deletedAt: Any? = null

            @SerializedName("is_notify")
            @Expose
            var isNotify: String? = null

            @SerializedName("coin")
            @Expose
            var coin: String? = null
        }
    }
}