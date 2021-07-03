package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class ViewProfileModel {
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

        @SerializedName("is_notify")
        @Expose
        var isNotify: String? = null

        @SerializedName("coin")
        @Expose
        var coin: String? = null

        @SerializedName("mall_link")
        @Expose
        var mallLink: String? = null

        @SerializedName("mall_category")
        @Expose
        var mallCategory: String? = null

        @SerializedName("mall_subcategory")
        @Expose
        var mallSubcategory: String? = null

        @SerializedName("set_image")
        @Expose
        var setImage: String? = null

        @SerializedName("token")
        @Expose
        var token: String? = null

        @SerializedName("category")
        @Expose
        var category: ArrayList<Category>? = null

        class Category {
            @SerializedName("id")
            @Expose
            var id: String? = null

            @SerializedName("parent_id")
            @Expose
            var parentId: String? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("sub_category")
            @Expose
            var subCategory: ArrayList<SubCategory>? = null

            class SubCategory {
                @SerializedName("id")
                @Expose
                var id: String? = null

                @SerializedName("parent_id")
                @Expose
                var parentId: String? = null

                @SerializedName("name")
                @Expose
                var name: String? = null
            }
        }
    }
}