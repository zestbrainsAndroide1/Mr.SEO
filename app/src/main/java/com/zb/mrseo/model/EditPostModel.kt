package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class EditPostModel {
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
        var platformId: String? = null

        @SerializedName("category_id")
        @Expose
        var categoryId: String? = null

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
        var registerPoint: String? = null

        @SerializedName("is_active")
        @Expose
        var isActive: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null
    }
}