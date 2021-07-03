package com.zb.mrseo.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class CategoryListModel {
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
        @SerializedName("category")
        @Expose
        var category: ArrayList<Category>? = null

        @SerializedName("bank")
        @Expose
        var bank: List<Bank>? = null

        class Bank {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("is_active")
            @Expose
            var isActive: String? = null
        }

        class Category {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("sub_category")
            @Expose
            var subCategory: ArrayList<SubCategory>? = null

            class SubCategory {
                @SerializedName("id")
                @Expose
                var id: Int? = null

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