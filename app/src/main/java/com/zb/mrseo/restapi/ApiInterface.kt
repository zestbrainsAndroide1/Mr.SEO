package com.zb.mrseo.restapi


import com.zb.mrseo.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @FormUrlEncoded
    @POST("V1/login")
    fun signIn(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("device_type") device_type: String,
        @Field("device_id") device_id: String,
        @Field("push_token") push_token: String
    ): Call<LoginModel>


    @GET("V1/user/getProfile")
    fun getProfile(
        @Header("Authorization") Authorization: String
    ): Call<ViewProfileModel>


    @Multipart
    @POST("V1/signup")
    fun signUp(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirm_password") confirm_password: RequestBody,
        @Part("name") name: RequestBody,
        @Part("nick_name") nick_name: RequestBody,
        @Part("country_code") country_code: RequestBody,
        @Part("device_id") device_id: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("device_type") device_type: RequestBody,
        @Part("bank_name") bank_name: RequestBody,
        @Part("account_number") account_number: RequestBody,
        @Part("push_token") push_token: RequestBody,
        @Part bank_image: MultipartBody.Part
    ): Call<SignUpModel>

    @FormUrlEncoded
    @POST("V1/forgot_password")
    fun getForgetPwd(
        @Field("email") email: String
    ): Call<ForgotPwdModel>


    @FormUrlEncoded
    @POST("V1/user/createSupport")
    fun getSupport(
        @Header("Authorization") Authorization: String,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("country_code") country_code: String,
        @Field("mobile") mobile: String,
        @Field("description") description: String
    ): Call<SupportModel>


    @POST("V1/post/getCategory")
    fun getCategory(
        @Header("Authorization") Authorization: String
    ): Call<CategoryModel>

    @GET("V1/post/getPlatform")
    fun getPlatformList(
        @Header("Authorization") Authorization: String
    ): Call<PlatformListModel>

    @FormUrlEncoded
    @POST("V1/post/createPosts")
    fun addContent(
        @Header("Authorization") Authorization: String,
        @Field("title") title: String,
        @Field("platform_id") platform_id: String,
        @Field("category_id") category_id: String,
        @Field("name") mall_name: String,
        @Field("description") description: String,
        @Field("register_point") register_point: String): Call<AddContentModel>


    @GET("V1/user/logout")
    fun logout(
        @Header("Authorization") Authorization: String
    ): Call<LogoutModel>


    @FormUrlEncoded
    @POST("V1/send_otp")
    fun sendOtp(
        @Field("mobile") mobile: String,
        @Field("country_code") country_code: String,
        @Field("device_id") device_id: String,
        @Field("push_token") push_token: String
    ): Call<SendOtpModel>

    @FormUrlEncoded
    @POST("V1/verify_otp")
    fun verifyOtp(
        @Field("mobile") mobile: String,
        @Field("otp") otp: String,
        @Field("country_code") country_code: String,
        @Field("device_id") device_id: String,
        @Field("push_token") push_token: String
    ): Call<VerifyOtpModel>


    @GET("V1/bankList")
    fun getBankList(): Call<BankListModel>

    @GET("V1/content/term_condition")
    fun getTerms(): Call<TermsModel>

    @POST("V1/post/home")
    fun getHomeData(
        @Header("Authorization") Authorization: String
    ): Call<HomeModel>

    @POST("V1/post/myPostList")
    fun getMyPosts(
        @Header("Authorization") Authorization: String
    ): Call<MyPostsModel>

    @GET("V1/post/viewPosts/{id}")
    fun getPostDetail(
        @Path("id") id: String,
        @Header("Authorization") Authorization: String
    ): Call<PostDetailModel>

    @Multipart
    @POST("V1/post/editPosts")
    fun editPost(
        @Header("Authorization") Authorization: String,
        @Part("title") title: RequestBody,
        @Part("platform_id") platform_id: RequestBody,
        @Part("category_id") category_id: RequestBody,
        @Part("mall_name") mall_name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("register_point") register_point: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("id") id: RequestBody
    ): Call<EditPostModel>

    @Multipart
    @POST("V1/user/updateProfile")
    fun editProfile(
        @Header("Authorization") Authorization: String,
        @Part("email") email: RequestBody,
        @Part("name") name: RequestBody,
        @Part("nick_name") nick_name: RequestBody,
        @Part("country_code") country_code: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("bank_name") bank_name: RequestBody,
        @Part("account_number") account_number: RequestBody,
        @Part bank_image: MultipartBody.Part): Call<EditProfileModel>


    @Multipart
    @POST("V1/user/updateProfile")
    fun editProfileWithProfile(
        @Header("Authorization") Authorization: String,
        @Part("email") email: RequestBody,
        @Part("name") name: RequestBody,
        @Part("nick_name") nick_name: RequestBody,
        @Part("country_code") country_code: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("bank_name") bank_name: RequestBody,
        @Part("account_number") account_number: RequestBody,
        @Part("info") info: RequestBody,
        @Part profile_image: MultipartBody.Part
    ): Call<EditProfileModel>

    @Multipart
    @POST("V1/user/updateProfile")
    fun editProfileWithBankImage(
        @Header("Authorization") Authorization: String,
        @Part("email") email: RequestBody,
        @Part("name") name: RequestBody,
        @Part("nick_name") nick_name: RequestBody,
        @Part("country_code") country_code: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("bank_name") bank_name: RequestBody,
        @Part("account_number") account_number: RequestBody,
        @Part("info") info: RequestBody,
        @Part bank_image: MultipartBody.Part
    ): Call<EditProfileModel>

    @FormUrlEncoded
    @POST("V1/user/updateProfile")
    fun editProfileWithData(
        @Header("Authorization") Authorization: String,
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("nick_name") nick_name: String,
        @Field("country_code") country_code: String,
        @Field("mobile") mobile: String,
        @Field("bank_name") bank_name: String,
        @Field("account_number") account_number:String): Call<EditProfileModel>

    @FormUrlEncoded
    @POST("V1/user/updateNotificationStatus")
    fun updateNotification(
        @Header("Authorization") Authorization: String,
        @Field("status") status: String,
        @Field("id") id: String
    ): Call<UpdateModel>

    @FormUrlEncoded
    @POST("V1/user/updateNotificationStatus")
    fun updateStatus(
        @Header("Authorization") Authorization: String,
        @Field("status") status: String): Call<UpdateStatusModel>

    @FormUrlEncoded
    @POST("V1/user/change_password")
    fun changePwd(
        @Header("Authorization") Authorization: String,
        @Field("old_password") old_password: String,
        @Field("password") password: String,
        @Field("confirm_password") confirm_password: String
    ): Call<ChangePwdModel>


    @FormUrlEncoded
    @POST("V1/post/get_post_details")
    fun getPost(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String): Call<PostModel>

    @FormUrlEncoded
    @POST("V1/post/createPosts")
    fun addBlog(
        @Header("Authorization") Authorization: String,
        @Field("category_id") category_id: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("register_point") register_point: String,
        @Field("keyword") keyword: String
    ): Call<AddBlogModel>

    @FormUrlEncoded
    @POST("V1/post/createPosts")
    fun addCafe(
        @Header("Authorization") Authorization: String,
        @Field("title") platform_id: String,
        @Field("category_id") category_id: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("register_point") register_point: String,
        @Field("url") url: String
    ): Call<AddCafeModel>


    @FormUrlEncoded
    @POST("V1/post/createPosts")
    fun addShop(
        @Header("Authorization") Authorization: String,
        @Field("platform_id") platform_id: String,
        @Field("category_id") category_id: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("register_point") register_point: String,
        @Field("keyword") keyword: String
    ): Call<AddShopModel>

    @POST("V1/post/myCompletedPostList")
    fun getMyHelperList(
        @Header("Authorization") Authorization: String
    ): Call<MyHelperModel>

    @FormUrlEncoded
    @POST("V1/post/help_post_details")
    fun getMyHelperDetail(
        @Header("Authorization") Authorization: String,
        @Field("help_id") help_id: String): Call<MyHelpDetailModel>

    @Multipart
    @POST("V1/post/upload_document")
    fun upload(
        @Header("Authorization") Authorization: String,
        @Part("help_id") help_id: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<UploadModel>

    @FormUrlEncoded
    @POST("V1/post/my_post_details")
    fun getMyPostDetail(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String): Call<MyPostDetailModel>

    @FormUrlEncoded
    @POST("V1/chat/thread_list")
    fun getChatList(
        @Header("Authorization") Authorization: String,
        @Field("limit") limit: String,
        @Field("offset") offset: String
    ): Call<ChatListModel>



    @GET("V1/user/coin_history")
    fun getTransactionList(
        @Header("Authorization") Authorization: String): Call<TransactionModel>

    @FormUrlEncoded
    @POST("V1/chat/chat_history")
    fun getChat(
        @Header("Authorization") Authorization: String,
        @Field("limit") limit: String,
        @Field("offset") offset: String,
        @Field("threads_id") threads_id: String


    ): Call<ChatModel>

    @FormUrlEncoded
    @POST("V1/chat/admin_chat_history")
    fun getAdminChat(
        @Header("Authorization") Authorization: String,
        @Field("limit") limit: String,
        @Field("offset") offset: String,
        @Field("message_type") type: String


    ): Call<AdminChatModel>

    @FormUrlEncoded
    @POST("V1/chat/chat")
    fun sendMsg(
        @Header("Authorization") Authorization: String,
        @Field("threads_id") threads_id: String,
        @Field("to_user_id") to_user_id: String,
        @Field("message") message: String,
        @Field("type") type: String): Call<SendMsgModel>

    @FormUrlEncoded
    @POST("V1/post/apply_for_help")
    fun applyForHelp(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String): Call<ApplyModel>

    @FormUrlEncoded
    @POST("V1/chat/admin_chat")
    fun sendAdminMsg(
        @Header("Authorization") Authorization: String,
        @Field("message") message: String,
        @Field("type") type: String,
        @Field("message_type") message_type: String,
    ): Call<AdminMsgModel>

    @FormUrlEncoded
    @POST("V1/post/editPosts")
    fun editCafe(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String,
        @Field("title") platform_id: String,
        @Field("category_id") category_id: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("register_point") register_point: String,
        @Field("url") url: String
    ): Call<EditModel>

    @FormUrlEncoded
    @POST("V1/post/editPosts")
    fun editBlog(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String,
        @Field("category_id") category_id: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("register_point") register_point: String,
        @Field("keyword") keyword: String
    ): Call<EditModel>


    @FormUrlEncoded
    @POST("V1/post/editPosts")
    fun editShopBuy(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String,
        @Field("platform_id") platform_id: String,
        @Field("category_id") category_id: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("register_point") register_point: String,
        @Field("keyword") keyword: String
    ): Call<EditModel>


    @FormUrlEncoded
    @POST("V1/post/editPosts")
    fun editContent(
        @Header("Authorization") Authorization: String,
        @Field("post_id") post_id: String,
        @Field("title") title: String,
        @Field("platform_id") platform_id: String,
        @Field("category_id") category_id: String,
        @Field("name") mall_name: String,
        @Field("description") description: String,
        @Field("register_point") register_point: String): Call<EditModel>

    @FormUrlEncoded
    @POST("V1/post/help_status_change")
    fun changeStatus(
        @Header("Authorization") Authorization: String,
        @Field("help_id") help_id: String,
        @Field("status") status: String): Call<StatusModel>

    @GET("V1/user/notice_list")
    fun getNotice(
        @Header("Authorization") Authorization: String): Call<NoticeModel>


    @POST("V1/chat/admin_chat_option")
    fun getStatus(
        @Header("Authorization") Authorization: String): Call<BtnStatusModel>


}





