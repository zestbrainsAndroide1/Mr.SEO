package com.zb.mrseo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.model.ChangePwdModel
import com.zb.mrseo.model.ForgotPwdModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_change_pwd.*
import kotlinx.android.synthetic.main.activity_forgot_pwd.*
import kotlinx.android.synthetic.main.activity_support.*
import kotlinx.android.synthetic.main.fragment_add_cafe.*

class ChangePwdActivity : AppCompatActivity(), ApiResponseInterface {

    var mUserModel: LoginModel.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pwd)
        setUi()
    }

    private fun setUi() {
        mUserModel = Prefs.getObject(
            this@ChangePwdActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        cv_submit.setOnClickListener {
            isValid()
        }
        img_back_change_pwd.setOnClickListener {
           onBackPressed()
        }

    }

    private fun isValid() {

        if (!isValidPassword(edt_old_pwd.text.toString())) {
            showToast(getString(R.string.email_validation), this@ChangePwdActivity)
        } else if (!isValidPassword(edt_new_pwd.text.toString())) {
            showToast(getString(R.string.email_validation1), this@ChangePwdActivity)

        } else if (!isValidPassword(edt_confirm_pwd.text.toString())) {
            showToast(getString(R.string.email_validation1), this@ChangePwdActivity)

        } else if (!edt_new_pwd.text.toString().equals(edt_confirm_pwd.text.toString())) {
            showToast(getString(R.string.email_validation1), this@ChangePwdActivity)

        } else {
            changePwd()
        }

    }

    private fun isValidPassword(password: String?): Boolean {
        return if (password != null && password.length >= 2 && password != "") {
            true
        } else false
    }

    private fun changePwd() {
        if (isNetworkAvailable(this@ChangePwdActivity)) {

            ApiRequest<Any>(
                activity = this@ChangePwdActivity,
                objectType = ApiInitialize.initialize()
                    .changePwd(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        edt_old_pwd.text.toString(),
                        edt_new_pwd.text.toString(),
                        edt_confirm_pwd.text.toString()

                    ),
                TYPE = WebConstant.CHANGE_PWD,
                isShowProgressDialog = true,
                apiResponseInterface = this@ChangePwdActivity
            )

        } else {
            SnackBar.show(
                this@ChangePwdActivity,
                true,
                getStr(this@ChangePwdActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.CHANGE_PWD -> {
                val response = apiResponseManager.response as ChangePwdModel

                when (response.status) {
                    200 -> {

                        ShowToast(response.message!!, this@ChangePwdActivity)
                        val intent = Intent(this@ChangePwdActivity, MainActivity::class.java)
                        intent.putExtra("show", "profile")
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()

                    }
                    else -> ShowToast(response.message!!, this@ChangePwdActivity)
                }
            }


        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@ChangePwdActivity, MainActivity::class.java)
        intent.putExtra("show", "profile")
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

}