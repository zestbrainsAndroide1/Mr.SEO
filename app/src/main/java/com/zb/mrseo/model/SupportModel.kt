package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SupportModel {
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
        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("country_code")
        @Expose
        var countryCode: String? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("mobile")
        @Expose
        var mobile: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("id")
        @Expose
        var id: Int? = null
    }
}