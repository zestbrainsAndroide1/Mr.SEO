package com.zb.mrseo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.model.DataModel
import com.zb.mrseo.model.SendOtpModel
import com.zb.mrseo.model.SignUpModel
import com.zb.mrseo.model.VerifyOtpModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_otp.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class OtpActivity : AppCompatActivity(), ApiResponseInterface {
    lateinit var dataModel: DataModel
    var token: String = ""
    var deviceId: String = ""
    var deviceType: String = "android"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        val extras = intent.extras
        if (extras != null) {
            dataModel = extras.getSerializable("data") as DataModel


        }
        deviceId = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        setUi()

    }

    private fun setUi() {
        tv_no.text=dataModel.mobile.toString()
        otp_view.otp = dataModel.otp.toString()

        cvVerify.setSafeOnClickListener {
            verifyOtp()
        }
    }

    private fun signUp() {

        if (isNetworkAvailable(this@OtpActivity)) {

            if ((dataModel.bankImg != "")) {

                val email1 = dataModel.email.toRequestBody("text/plain".toMediaTypeOrNull())
                val password1 = dataModel.password.toRequestBody("text/plain".toMediaTypeOrNull())
                val name1 = dataModel.name.toRequestBody("text/plain".toMediaTypeOrNull())
                val nickName1 = dataModel.nickname.toRequestBody("text/plain".toMediaTypeOrNull())
                val countryCode1 =
                    dataModel.countryCode.toRequestBody("text/plain".toMediaTypeOrNull())
                val deviceId1 = deviceId.toRequestBody("text/plain".toMediaTypeOrNull())
                val mobile1 = dataModel.mobile.toRequestBody("text/plain".toMediaTypeOrNull())

                val deviceType1 = deviceType.toRequestBody("text/plain".toMediaTypeOrNull())
                val bankName1 = dataModel.bankName.toRequestBody("text/plain".toMediaTypeOrNull())
                val accNo1 = dataModel.accNo.toRequestBody("text/plain".toMediaTypeOrNull())
                val token1 = token.toRequestBody("text/plain".toMediaTypeOrNull())


                    val requestFile1: RequestBody
                    var profileImage1: MultipartBody.Part? = null
                    requestFile1 = File(dataModel.bankImg).asRequestBody("image/*".toMediaTypeOrNull())
                    profileImage1 =
                        MultipartBody.Part.createFormData(
                            "bank_image",
                            "profile.jpg",
                            requestFile1
                        )

                    ApiRequest<Any>(
                        activity = this@OtpActivity,
                        objectType = ApiInitialize.initialize()
                            .signUp(
                                email1,
                                password1,
                                password1,
                                name1,
                                nickName1,
                                countryCode1,
                                deviceId1,
                                mobile1,
                                deviceType1,
                                bankName1,
                                accNo1,
                                token1,
                                profileImage1

                            ),
                        TYPE = WebConstant.SIGN_UP,
                        isShowProgressDialog = true,
                        apiResponseInterface = this@OtpActivity
                    )


            }
        } else {
            SnackBar.show(
                this@OtpActivity,
                true,
                getStr(this@OtpActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }

    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.SIGN_UP -> {
                val response = apiResponseManager.response as SignUpModel

                when (response.status) {
                    200 -> {


                        Prefs.setObject(
                            context = this@OtpActivity,
                            key = AppConstant.ACCOUNT_DATA,
                            value = response.data!!
                        )
                        Prefs.setValue(
                            context = this@OtpActivity,
                            key = AppConstant.USER_ID,
                            value = response.data!!.id.toString()
                        )

                        ShowToast(response.message!!, this@OtpActivity)
                        start<MainActivity>()
                        finishAffinity()


                    }
                    else -> ShowToast(response.message!!, this@OtpActivity)
                }
            }

            WebConstant.VERIFY_OTP -> {
                val response = apiResponseManager.response as VerifyOtpModel

                when (response.status) {
                    200 -> {

                        ShowToast(response.message!!, this@OtpActivity)

                        signUp()

                    }
                    else -> ShowToast(response.message!!, this@OtpActivity)
                }
            }

        }
    }


    override fun onBackPressed() {
        start<SignUpActivity>()
    }

    private fun verifyOtp() {
        if (isNetworkAvailable(this@OtpActivity)) {

            ApiRequest<Any>(
                activity = this@OtpActivity,
                objectType = ApiInitialize.initialize()
                    .verifyOtp(
                        dataModel.mobile,
                        dataModel.otp,
                        dataModel.countryCode,
                        "1",
                        token

                    ),
                TYPE = WebConstant.VERIFY_OTP,
                isShowProgressDialog = true,
                apiResponseInterface = this@OtpActivity
            )

        } else {
            SnackBar.show(
                this@OtpActivity,
                true,
                getStr(this@OtpActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

}