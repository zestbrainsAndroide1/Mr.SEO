package com.zb.mrseo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zb.moodlist.utility.*
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.SupportModel
import com.zb.mrseo.model.ViewProfileModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.ccp_login_country
import kotlinx.android.synthetic.main.activity_support.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.regex.Pattern

class SupportActivity : AppCompatActivity(), ApiResponseInterface {

    private var email: String = ""
    private var name: String = ""
    private var countryCode: String = ""
    private var mobile: String = ""
    private var desc: String = ""
    var mUserModel: LoginModel.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
        setUi()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun setUi() {
        mUserModel = Prefs.getObject(
            this@SupportActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data
/*
        tv_country_code.text = getCountryCode()
*/
        cv_send.setSafeOnClickListener {
            countryCode = getCountryCode()
            email = edt_email_support.text.toString()
            name = edt_name_support.text.toString()
            mobile = edt_phone_no.text.toString()
            desc = edt_desc.text.toString()

            if (!isValidEmail1(email)) {
                showToast(getString(R.string.email_validation1), this@SupportActivity)

            } else if (!isValidEmail(email)) {

                showToast(getString(R.string.email_validation), this@SupportActivity)


            } else if (name.equals("")) {

                showToast(getString(R.string.name_validation), this@SupportActivity)


            } else if (desc.equals("")) {

                showToast(getString(R.string.desc_validation), this@SupportActivity)


            } else if (!isValidNumber(mobile)) {

                showToast(getString(R.string.mobile_validation), this@SupportActivity)


            } else {
                getSupport()
            }
        }

        img_back_support.setSafeOnClickListener {
            onBackPressed()
        }

    }

    private fun getSupport() {
        if (isNetworkAvailable(this@SupportActivity)) {

            ApiRequest<Any>(
                activity = this@SupportActivity,
                objectType = ApiInitialize.initialize()
                    .getSupport(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        email,
                        name,
                        countryCode,
                        mobile,
                        desc
                    ),
                TYPE = WebConstant.GET_SUPPPORT,
                isShowProgressDialog = true,
                apiResponseInterface = this@SupportActivity
            )

        } else {
            SnackBar.show(
                this@SupportActivity,
                true,
                getStr(this@SupportActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_SUPPPORT -> {
                val response = apiResponseManager.response as SupportModel

                when (response.status) {
                    200 -> {
                        onBackPressed()


                    }
                    else -> ShowToast(response.message!!, this@SupportActivity)
                }
            }


        }
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

    private fun getCountryCode(): String {
        return ccp_login_country.selectedCountryCodeWithPlus.toString().trim()
    }

    // validating Number
    private fun isValidNumber(number: String?): Boolean {
        return if (number != null && number.length == 10) {
            true
        } else false
    }
}