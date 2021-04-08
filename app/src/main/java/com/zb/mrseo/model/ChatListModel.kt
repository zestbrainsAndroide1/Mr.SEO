package com.zb.mrseo.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ChatListModel {
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
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("friend_id")
        @Expose
        var friendId: Int? = null

        @SerializedName("is_active")
        @Expose
        var isActive: String? = null

        @SerializedName("user_status")
        @Expose
        var userStatus: String? = null

        @SerializedName("friend_status")
        @Expose
        var friendStatus: String? = null

        @SerializedName("chat_type")
        @Expose
        var chatType: String? = null

        @SerializedName("message_latest")
        @Expose
        var messageLatest: MessageLatest? = null

        @SerializedName("sender_user")
        @Expose
        var senderUser: SenderUser? = null

        @SerializedName("receiver_user")
        @Expose
        var receiverUser: ReceiverUser? = null

        class MessageLatest {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("threads_id")
            @Expose
            var threadsId: Int? = null

            @SerializedName("sender_id")
            @Expose
            var senderId: Int? = null

            @SerializedName("receiver_id")
            @Expose
            var receiverId: Int? = null

            @SerializedName("message")
            @Expose
            var message: String? = null

            @SerializedName("file")
            @Expose
            var file: String? = null

            @SerializedName("type")
            @Expose
            var type: String? = null

            @SerializedName("status")
            @Expose
            var status: String? = null

            @SerializedName("sender_status")
            @Expose
            var senderStatus: String? = null

            @SerializedName("receiver_status")
            @Expose
            var receiverStatus: String? = null

            @SerializedName("created_at")
            @Expose
            var createdAt: String? = null
        }

        class ReceiverUser {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("email")
            @Expose
            var email: String? = null

            @SerializedName("avatar")
            @Expose
            var avatar: String? = null
        }

        class SenderUser {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("email")
            @Expose
            var email: String? = null

            @SerializedName("avatar")
            @Expose
            var avatar: String? = null
        }
    }
}