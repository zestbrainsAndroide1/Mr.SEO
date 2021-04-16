package com.zb.mrseo.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.iid.FirebaseInstanceId
import com.zb.mindme.permission.PermissionChecker
import com.zb.mindme.permission.PermissionStatus
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.adapter.BankListAdapter
import com.zb.mrseo.adapter.PlatformAdapter
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.*
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_forgot_pwd.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.ccp_login_country
import kotlinx.android.synthetic.main.activity_sign_up.edtAccNo
import kotlinx.android.synthetic.main.dialog_terms.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.sufficientlysecure.htmltextview.HtmlTextView
import java.io.File
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(), PermissionStatus, ApiResponseInterface,
    OnPlatformClick {
    lateinit var dialog: Dialog
    lateinit var dialog1: Dialog

    lateinit var bankListAdapter: BankListAdapter

    private var profilePath: String = ""

    private var email: String = ""
    private var password: String = ""
    private var name: String = ""
    private var nickName: String = ""
    private var countryCode: String = "+82"
    private var deviceId: String = ""
    private var mobile: String = ""
    private var deviceType: String = "Android"
    private var bankName: String = ""
    private var accNo: String = ""
    private var info: String = ""
    private var token: String = ""
    private var confirmPassword: String = ""
    var tvDesc: HtmlTextView? = null
    private var isProfile: String = ""

    private var bankImagePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        deviceId = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        setUi()
    }

    private fun setUi() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val newToken = instanceIdResult.token
            Log.e("Push", newToken)
            token = newToken
        }
        bankDialog()
        termsDialog()
/*
        tvCountry.text = getCountryCode()
*/
        cvSignUp.setSafeOnClickListener {
            email = edt_email.text.toString()
            password = edt_password.text.toString()
            name = edtName.text.toString()
            nickName = edtNickName.text.toString()
            mobile = edtPhone.text.toString()
            bankName = edtBankName.text.toString()
            accNo = edtAccNo.text.toString()
            confirmPassword = edtConfirmPassword.text.toString()

            if (!isValidEmail1(email)) {
                showToast(getString(R.string.email_validation1), this@SignUpActivity)

            } else if (!isValidEmail(email)) {

                showToast(getString(R.string.email_validation), this@SignUpActivity)


            } else if (name.equals("")) {

                showToast(getString(R.string.name_validation), this@SignUpActivity)


            } else if (nickName.equals("")) {

                showToast(getString(R.string.nick_name_validation), this@SignUpActivity)


            } else if (bankName.equals("")) {

                showToast(getString(R.string.bank_name_validation), this@SignUpActivity)


            } else if (!isValidNumber(mobile)) {

                showToast(getString(R.string.mobile_validation), this@SignUpActivity)


            } else if (!password.equals(confirmPassword)) {

                showToast(getString(R.string.pwd_validation1), this@SignUpActivity)


            } else if (!chk_accept.isChecked == true) {

                showToast(getString(R.string.terms_validation), this@SignUpActivity)


            } else if (!isValidPassword(password)) {
                showToast(getString(R.string.pwd_validation), this@SignUpActivity)

            } else {
                /* signUp()*/
                sendOtp()
            }
        }
        llAccept.setSafeOnClickListener {
            dialog.show()
        }
        edtBankName.setSafeOnClickListener {
            dialog1.show()
        }

        cl_bank_img_sign_up.setOnClickListener(View.OnClickListener {
            isProfile = "false"
            PermissionChecker(
                this@SignUpActivity, this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        })
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

    private fun termsDialog() {

        dialog = Dialog(this@SignUpActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_terms)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp


        val tvYes = dialog.findViewById(R.id.cvOk) as CardView
        tvDesc = dialog.findViewById(R.id.tv_desc)

        getTerms()

        tvYes.setOnClickListener(View.OnClickListener {
            dialog.dismiss()

        })

    }

    override fun allGranted() {
        ImagePicker.with(this@SignUpActivity)
            .crop(16f, 16f)
            .compress(1024)
            //Final image size will be less than 1 MB(Optional)
            .start()
    }

    override fun onDenied() {
        //ShowToast.show(mContext, getStr(mContext, R.string.deny_permission))
    }

    override fun foreverDenied() {
        SnackBar.show(this@SignUpActivity,
            false,
            getStr(this@SignUpActivity, R.string.str_allow_storage),
            true,
            getStr(this@SignUpActivity, R.string.enable),
            View.OnClickListener {})
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {


            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data

            //You can get File object from intent
            val file: File = com.github.dhaval2404.imagepicker.ImagePicker.getFile(data)!!

            //You can also get File Path from intent
            bankImagePath = ImagePicker.getFilePath(data)!!
            img_camera.gone()
            img_bank_sign_up.setImageBitmap(null)
            img_bank_sign_up!!.setImageURI(fileUri)


        } else if (resultCode == com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR) {
            Toast.makeText(
                this@SignUpActivity,
                com.github.dhaval2404.imagepicker.ImagePicker.getError(data),
                Toast.LENGTH_SHORT
            ).show()
        } else {

        }


    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {


            WebConstant.SEND_OTP -> {
                val response = apiResponseManager.response as SendOtpModel

                when (response.status) {
                    200 -> {


                        val intent = Intent(this@SignUpActivity, OtpActivity::class.java)
                        intent.putExtra(
                            "data", DataModel(
                                email,
                                password,
                                name,
                                nickName,
                                countryCode,
                                mobile,
                                bankName,
                                accNo,
                                response.data!!.otp.toString(),
                                bankImagePath
                            )
                        )
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()


                    }
                    else -> ShowToast(response.message!!, this@SignUpActivity)
                }
            }

            WebConstant.GET_BANK_LIST -> {
                val response = apiResponseManager.response as BankListModel

                when (response.status) {
                    200 -> {

                        bankListAdapter.clear()
                        if (response.data!!.size > 0) {
                            bankListAdapter.addAll(response.data!!)
                        } else {

                        }

                    }
                    else -> ShowToast(response.message!!, this@SignUpActivity)
                }
            }
            WebConstant.GET_TERMS -> {
                val response = apiResponseManager.response as TermsModel

                when (response.status) {
                    200 -> {

                        tvDesc!!.setHtml(response.data!!.get(0).content.toString())


                    }
                    else -> ShowToast(response.message!!, this@SignUpActivity)
                }
            }


        }
    }

    // validating Number
    private fun isValidNumber(number: String?): Boolean {
        return if (number != null && number.length == 10) {
            true
        } else false
    }



    private fun sendOtp() {
        if (isNetworkAvailable(this@SignUpActivity)) {

            ApiRequest<Any>(
                activity = this@SignUpActivity,
                objectType = ApiInitialize.initialize()
                    .sendOtp(
                        mobile,
                        countryCode,
                        "1",
                        token

                    ),
                TYPE = WebConstant.SEND_OTP,
                isShowProgressDialog = true,
                apiResponseInterface = this@SignUpActivity
            )

        } else {
            SnackBar.show(
                this@SignUpActivity,
                true,
                getStr(this@SignUpActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    private fun bankDialog() {

        dialog1 = Dialog(this@SignUpActivity)
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialog1.setCancelable(false)
        dialog1.setContentView(R.layout.dialog_platform)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog1.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog1.window!!.attributes = lp
        val imgClose = dialog1.findViewById(R.id.img_close) as ImageView
        val rvBank = dialog1.findViewById(R.id.rvPlatform) as RecyclerView
        val tvTitle = dialog1.findViewById(R.id.tv_title) as TextView
        tvTitle.text = getString(R.string.select_bank)

        bankListAdapter = BankListAdapter(this@SignUpActivity, this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@SignUpActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        rvBank?.layoutManager = linearLayoutManager1
        rvBank?.adapter = bankListAdapter
        getBankList()
        imgClose.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
        })


    }

    private fun getBankList() {
        if (isNetworkAvailable(this@SignUpActivity)) {

            ApiRequest<Any>(
                activity = this@SignUpActivity,
                objectType = ApiInitialize.initialize()
                    .getBankList(),
                TYPE = WebConstant.GET_BANK_LIST,
                isShowProgressDialog = false,
                apiResponseInterface = this@SignUpActivity
            )

        } else {
            SnackBar.show(
                this@SignUpActivity,
                true,
                getStr(this@SignUpActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onPlatformCllick(position: Int, title: String, id: String) {
        bankName = title.toString()
        edtBankName.text = title.toString().toEditable()
        dialog1.dismiss()
    }

    private fun getTerms() {
        if (isNetworkAvailable(this@SignUpActivity)) {

            ApiRequest<Any>(
                activity = this@SignUpActivity,
                objectType = ApiInitialize.initialize()
                    .getTerms(),
                TYPE = WebConstant.GET_TERMS,
                isShowProgressDialog = false,
                apiResponseInterface = this@SignUpActivity
            )

        } else {
            SnackBar.show(
                this@SignUpActivity,
                true,
                getStr(this@SignUpActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

}