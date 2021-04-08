package com.zb.mrseo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.model.ForgotPwdModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_forgot_pwd.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

class ForgotPwdActivity : AppCompatActivity() ,ApiResponseInterface{
    var email: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pwd)
        setUi()
    }


    private fun setUi() {
       cvSend.setOnClickListener {

            email = edt_email_id.text.toString()

            if (!isValidEmail(email)) {
                showToast(getString(R.string.email_validation), this@ForgotPwdActivity)
            } else if (!isValidEmail1(email)) {
                showToast(getString(R.string.email_validation1), this@ForgotPwdActivity)

            } else {
                getForgetPwd()
            }

        }

       tv_back.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this@ForgotPwdActivity, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
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

    private fun getForgetPwd() {
        if (isNetworkAvailable(this@ForgotPwdActivity)) {

            ApiRequest<Any>(
                activity = this@ForgotPwdActivity,
                objectType = ApiInitialize.initialize()
                    .getForgetPwd(
                        email

                    ),
                TYPE = WebConstant.GET_FORGET_PWD,
                isShowProgressDialog = true,
                apiResponseInterface = this@ForgotPwdActivity
            )

        } else {
            SnackBar.show(
                this@ForgotPwdActivity,
                true,
                getStr(this@ForgotPwdActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_FORGET_PWD -> {
                val response = apiResponseManager.response as ForgotPwdModel

                when (response.status) {
                    200 -> {

                        ShowToast(response.message!!, this@ForgotPwdActivity)
                        val intent = Intent(this@ForgotPwdActivity, LoginActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()

                    }
                    else -> ShowToast(response.message!!, this@ForgotPwdActivity)
                }
            }


        }
    }


}