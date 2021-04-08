package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class SignUpModel {
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
        var coin: Any? = null

        @SerializedName("token")
        @Expose
        var token: String? = null
    }
}