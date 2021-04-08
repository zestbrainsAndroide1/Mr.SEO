package com.zb.mrseo.activity

import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern


class LoginActivity : AppCompatActivity(), ApiResponseInterface {
    var email: String = ""
    var password: String = ""
    var token: String = ""
    var deviceId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
      deviceId = Secure.getString(
            this.contentResolver,
            Secure.ANDROID_ID
        )
        setUi()

    }

    private fun setUi() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.i("SEO", "Token Failed==>", task.exception)
                    return@OnCompleteListener
                }
                token = task.result.toString()
                Log.i("SEO", token)
            })

//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
//            val newToken = instanceIdResult.token
//            Log.e("Push", newToken)
//            token = newToken
//        }
        chk_remember_me.setOnClickListener(View.OnClickListener { v ->
            val checked = (v as CheckBox).isChecked
            // Check which checkbox was clicked
            if (checked) {
                Prefs1.setValue(this@LoginActivity, AppConstant.REMEMBER_ME, "true")
                Prefs1.setValue(this@LoginActivity, AppConstant.USER_EMAIL, edtEmailId.text.toString())
                Prefs1.setValue(this@LoginActivity, AppConstant.USER_PASSWORD, edtPassword.text.toString())


            } else {
                Prefs1.clearAllData(this@LoginActivity)

            }
        })


        cvLogIn.setOnClickListener {
            email = edtEmailId.text.toString()
            password = edtPassword.text.toString()

            if(chk_remember_me.isChecked){
                Prefs1.setValue(this@LoginActivity, AppConstant.USER_EMAIL, edtEmailId.text.toString())
                Prefs1.setValue(this@LoginActivity, AppConstant.USER_PASSWORD, edtPassword.text.toString())

            }else{

            }
            if (!isValidEmail1(email)) {
                showToast(getString(R.string.email_validation1), this@LoginActivity)

            } else if (!isValidEmail(email)) {

                showToast(getString(R.string.email_validation), this@LoginActivity)


            } else if (!isValidPassword(password)) {
                showToast(getString(R.string.pwd_validation), this@LoginActivity)

            } else {
                signIn()
            }

        }

        tv_forgot_pwd.setSafeOnClickListener {
            start<ForgotPwdActivity>()
        }
        ll_sign_up.setSafeOnClickListener {
            start<SignUpActivity>()
        }

    }

    override fun onBackPressed() {
        finish()
    }

    private fun isValidEmail1(email: String?): Boolean {
        return if (email != null && email.length > 2 && email != "") {
            true
        } else
            false

    }

    private fun isValidEmail(email: String): Boolean {
        val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }


    private fun isValidPassword(password: String?): Boolean {
        return if (password != null && password.length >= 2 && password != "") {
            true
        } else false
    }

    private fun signIn() {
        if (isNetworkAvailable(this@LoginActivity)) {

            ApiRequest<Any>(
                activity = this@LoginActivity,
                objectType = ApiInitialize.initialize()
                    .signIn(
                        email,
                        password,
                        "android",
                        deviceId,
                        token

                    ),
                TYPE = WebConstant.SIGN_IN,
                isShowProgressDialog = true,
                apiResponseInterface = this@LoginActivity
            )

        } else {
            SnackBar.show(
                this@LoginActivity,
                true,
                getStr(this@LoginActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.SIGN_IN -> {
                val response = apiResponseManager.response as LoginModel

                when (response.status) {
                    200 -> {
                        if (chk_remember_me.isChecked) {
                            Prefs1.setValue(this@LoginActivity, AppConstant.REMEMBER_ME, "true")
                            Prefs1.setValue(
                                this@LoginActivity,
                                AppConstant.USER_EMAIL,
                                response.data!!.email.toString()
                            )

                        } else {


                        }

                        Prefs.setObject(
                            context = this@LoginActivity,
                            key = AppConstant.ACCOUNT_DATA,
                            value = response.data!!
                        )

                        Prefs.setValue(
                            context = this@LoginActivity,
                            key = AppConstant.USER_ID,
                            value = response.data!!.id.toString()
                        )

                        ShowToast(response.message!!, this@LoginActivity)
                        start<MainActivity>( "img" to response.data!!.setImg.toString())
                        finishAffinity()


                    }
                    else -> ShowToast(response.message!!, this@LoginActivity)
                }
            }


        }
    }
}