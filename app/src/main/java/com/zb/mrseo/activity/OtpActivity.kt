package com.zb.mrseo.activity

import `in`.aabhasjindal.otptextview.OTPListener
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.model.DataModel
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
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity(), ApiResponseInterface {
    lateinit var dataModel: DataModel
    var token: String = ""
    var deviceId: String = ""
    var deviceType: String = "android"
     var OTP:String=""
    var number: String = ""
    var isCheck=false

    private var mAuth: FirebaseAuth? = null
    private var mVerificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        val extras = intent.extras
        if (extras != null) {
            dataModel = extras.getSerializable("data") as DataModel


        }


        //initializing objects

        //initializing objects
        mAuth = FirebaseAuth.getInstance()
        sendVerificationCode(dataModel.mobile)

        deviceId = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        setUi()

    }

    private fun setUi() {
        var num = 0
        tv_no.text="+82 "+dataModel.mobile.toString()
        otp_view.otpListener = object : OTPListener {
            override fun onInteractionListener() {
               /* num++
                Log.d("num : ", num.toString())
                if(num == 6){
                    otp_button.setBackgroundColor(Color.parseColor("#4EA3FE"))
                    otp_button.isEnabled = true
                    otp_button.isClickable = true
                } else {
                    otp_button.setBackgroundColor(Color.parseColor("#777777"))
                    otp_button.isEnabled = false
                }*/
            }

            override fun onOTPComplete(otp: String) {
                // fired when user has entered the OTP fully.
                Log.d("onOTPComplete : ", otp.toString())
                OTP = otp
            }
        }
        otp_button.setSafeOnClickListener {
            isCheck=true
            if(!OTP.equals(number)){
                Log.d("otp plz : ", number.toString())
                showToast(getString(R.string.otp_validation),this@OtpActivity)
            }else{
                signUp()
            }
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


                val mallLink = dataModel.url.toRequestBody("text/plain".toMediaTypeOrNull())
                val mallCategory = dataModel.mallCategory.toRequestBody("text/plain".toMediaTypeOrNull())
                val mallSubCategory = dataModel.mallSubCategory.toRequestBody("text/plain".toMediaTypeOrNull())


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
                                profileImage1,
                                mallLink,
                                mallCategory,
                                mallSubCategory
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
                        start<LoginActivity>()
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
                        OTP,
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


    private fun sendVerificationCode(mobile: String) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+82" + mobile,      // Phone number to verify
            60,               // Timeout duration
            TimeUnit.SECONDS, // Unit of timeout
            this,            // Activity (for callback binding)
            mCallbacks
        )

    }
    //the callback to detect the verification status
    private val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                //Getting the code sent by SMS
                val code: String = phoneAuthCredential.smsCode

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    verifyVerificationCode(code)
                    number = code
                    Log.d("code : ", number.toString())
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.e("otpMessage",e.message.toString())
                Toast.makeText(this@OtpActivity, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken?
            ) {
                super.onCodeSent(s, forceResendingToken)

                //storing the verification id that is sent to the user
                mVerificationId = s
            }
        }


    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                OnCompleteListener<Any?> { task ->
                    if (task.isSuccessful) {
                              signUp()
                        //verification successful we will start the profile activity
                        Toast.makeText(this@OtpActivity, "Success", Toast.LENGTH_SHORT).show()
                    } else {
                        //verification unsuccessful.. display an error message
                        var message = "Somthing is wrong, we will fix it soon..."
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered..."
                        }
                        val snackbar = Snackbar.make(
                            findViewById(R.id.parent),
                            message, Snackbar.LENGTH_LONG
                        )
                        snackbar.setAction("Dismiss") { }
                        snackbar.show()
                    }
                }
            }
    }
}