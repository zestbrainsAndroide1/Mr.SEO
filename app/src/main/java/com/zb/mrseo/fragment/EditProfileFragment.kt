package com.zb.mrseo.fragment

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.zb.mindme.permission.PermissionChecker
import com.zb.mindme.permission.PermissionStatus
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.activity.LoginActivity
import com.zb.mrseo.adapter.BankListAdapter
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.*
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.KeyboardUtils
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_edit_profile.edtAccNo
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MediaType.Companion.get
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.regex.Pattern


class EditProfileFragment : Fragment(), ApiResponseInterface, OnPlatformClick, PermissionStatus {

    var mUserModel: LoginModel.Data? = null
    lateinit var dialog: Dialog
    lateinit var bankListAdapter: BankListAdapter

    private var profilePath: String = ""

    private var email: String = ""
    private var password: String = ""
    private var name: String = ""
    private var nickName: String = ""
    private var countryCode: String = "+82"
    private var deviceId: String = "1"
    private var mobile: String = ""
    private var deviceType: String = "Android"
    private var bankName: String = ""
    private var accNo: String = ""
    private var info: String = ""
    private var token: String = ""
    private var confirmPassword: String = ""
    private var isProfile: String = ""
    private var bankProfilePath: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUi()
    }

    private fun setUi() {
        try {
            KeyboardUtils.addKeyboardToggleListener(activity!!, object :
                KeyboardUtils.SoftKeyboardToggleListener {
                override fun onToggleSoftKeyboard(isVisible: Boolean) {
                    if (isVisible) {
                        MainActivity.cvMain!!.visibility = View.GONE

                    } else {
                        MainActivity.cvMain!!.visibility = View.VISIBLE
                    }
                }
            })
        } catch (e: Exception) {

        }
        mUserModel = Prefs.getObject(
            activity!!,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data
        bankDialog()
        edt_bank_name_edit_profile.setSafeOnClickListener {
            dialog.show()
        }

        img_bank_edit.setOnClickListener(View.OnClickListener {
            PermissionChecker(
                activity!!, this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        })
        cvAddEditProfile.setSafeOnClickListener {
            email = edt_email_edit_profile.text.toString()
            name = edtUserName.text.toString()
            nickName = edt_nick_name.text.toString()
            mobile = edt_phone_edit_profile.text.toString()
            bankName = edt_bank_name_edit_profile.text.toString()
            accNo = edtAccNo.text.toString()

            if (!isValidEmail1(email)) {
                showToast(getString(R.string.email_validation1), activity!!)

            } else if (!isValidEmail(email)) {

                showToast(getString(R.string.email_validation), activity!!)


            } else if (name.equals("")) {

                showToast(getString(R.string.name_validation), activity!!)


            } else if (nickName.equals("")) {

                showToast(getString(R.string.nick_name_validation), activity!!)


            } else if (bankName.equals("")) {

                showToast(getString(R.string.bank_name_validation), activity!!)


            } else if (!isValidNumber(mobile)) {

                showToast(getString(R.string.mobile_validation), activity!!)


            }  else {
                editProfile()
            }
        }

        getProfile()


        img_back_edit_profile.setOnClickListener(View.OnClickListener {
            val someFragment: Fragment = ProfileFragment()
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        })
    }

    private fun editProfile() {

        if (isNetworkAvailable(activity!!)) {

            if (!profilePath.equals("")) {

                val email1 = email.toRequestBody("text/plain".toMediaTypeOrNull())
                val name1 = name.toRequestBody("text/plain".toMediaTypeOrNull())
                val nickName1 = nickName.toRequestBody("text/plain".toMediaTypeOrNull())
                val countryCode1 =
                   countryCode.toRequestBody("text/plain".toMediaTypeOrNull())
                val deviceId1 = deviceId.toRequestBody("text/plain".toMediaTypeOrNull())
                val mobile1 = mobile.toRequestBody("text/plain".toMediaTypeOrNull())

                val deviceType1 = deviceType.toRequestBody("text/plain".toMediaTypeOrNull())
                val bankName1 = bankName.toRequestBody("text/plain".toMediaTypeOrNull())
                val accNo1 = accNo.toRequestBody("text/plain".toMediaTypeOrNull())
                val info1 = info.toRequestBody("text/plain".toMediaTypeOrNull())
                val token1 = token.toRequestBody("text/plain".toMediaTypeOrNull())

                val requestFile: RequestBody
                var profileImage: MultipartBody.Part? = null

                requestFile = File(profilePath).asRequestBody("image/*".toMediaTypeOrNull())
                profileImage =
                    MultipartBody.Part.createFormData(
                        "bank_image",
                        "profile.jpg",
                        requestFile
                    )




                ApiRequest<Any>(
                    activity = activity!!,
                    objectType = ApiInitialize.initialize()
                        .editProfile(
                            "Bearer ".plus(mUserModel!!.token.toString()),
                            email1,
                            name1,
                            nickName1,
                            countryCode1,
                            mobile1,
                            bankName1,
                            accNo1,
                            profileImage
                        ),
                    TYPE = WebConstant.EDIT_PROFILE,
                    isShowProgressDialog = true,
                    apiResponseInterface = this@EditProfileFragment
                )

            } else if (profilePath.equals("")) {


                ApiRequest<Any>(
                    activity = activity!!,
                    objectType = ApiInitialize.initialize()
                        .editProfileWithData(
                            "Bearer ".plus(mUserModel!!.token.toString()),
                            email,
                            name,
                            nickName,
                            countryCode,
                            mobile,
                            bankName,
                            accNo,

                            ),
                    TYPE = WebConstant.EDIT_PROFILE,
                    isShowProgressDialog = true,
                    apiResponseInterface = this@EditProfileFragment
                )


            } else {


            }


        } else {
            SnackBar.show(
                activity!!,
                true,
                getStr(activity!!, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }

    }




    private fun getProfile() {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .getProfile(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_PROFILE,
                isShowProgressDialog = true,
                apiResponseInterface = this@EditProfileFragment
            )

        } else {
            SnackBar.show(
                activity!!,
                true,
                getStr(activity!!, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_PROFILE -> {
                val response = apiResponseManager.response as ViewProfileModel

                when (response.status) {
                    200 -> {

                        edt_email_edit_profile.text = response.data!!.email.toString().toEditable()
                        edtUserName.text = response.data!!.name.toString().toEditable()
                        edt_nick_name.text = response.data!!.nickName.toString().toEditable()
                        edt_phone_edit_profile.text = response.data!!.mobile.toString().toEditable()
                        edt_bank_name_edit_profile.text =
                            response.data!!.bankName.toString().toEditable()
                        edtAccNo.text = response.data!!.accountNumber.toString().toEditable()

                        Glide.with(this@EditProfileFragment)
                            .load(response.data!!.bankImage.toString())
                            .into(img_bank_edit)


                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }

            WebConstant.LOGOUT -> {
                val response = apiResponseManager.response as LogoutModel

                when (response.status) {
                    200 -> {

                        Prefs.clearAllData(activity!!)
                        val intent = Intent(activity!!, LoginActivity::class.java)
                        startActivity(intent)
                        activity!!.overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )
                        activity!!.finish()
                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }

            WebConstant.EDIT_PROFILE -> {
                val response = apiResponseManager.response as EditProfileModel

                when (response.status) {
                    200 -> {
                        ShowToast(response.message!!, activity!!)
                        val intent = Intent(activity!!, MainActivity::class.java)
                        intent.putExtra("show", "profile")
                        activity!!.startActivity(intent)
                        activity!!.overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )
                        activity!!.finish()

                    }
                    else -> ShowToast(response.message!!, activity!!)
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
                    else -> ShowToast(response.message!!, requireActivity())
                }
            }

        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun bankDialog() {

        dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_platform)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        val imgClose = dialog.findViewById(R.id.img_close) as ImageView
        val rvBank = dialog.findViewById(R.id.rvPlatform) as RecyclerView
        val tvTitle = dialog.findViewById(R.id.tv_title) as TextView
        tvTitle.text = getString(R.string.select_bank)

        bankListAdapter = BankListAdapter(activity!!, this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                activity!!,
                LinearLayoutManager.VERTICAL,
                false
            )
        rvBank?.layoutManager = linearLayoutManager1
        rvBank?.adapter = bankListAdapter
        getBankList()
        imgClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })


    }

    private fun getBankList() {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .getBankList(),
                TYPE = WebConstant.GET_BANK_LIST,
                isShowProgressDialog = false,
                apiResponseInterface = this@EditProfileFragment
            )

        } else {
            SnackBar.show(
                activity!!,
                true,
                getStr(activity!!, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun onPlatformCllick(position: Int, title: String, id: String) {
        bankName = title.toString()
        edt_bank_name_edit_profile.text = title.toString().toEditable()
        dialog.dismiss()
    }

    override fun allGranted() {
        ImagePicker.with(this@EditProfileFragment)
            .crop(16f, 16f)
            .compress(1024)
            //Final image size will be less than 1 MB(Optional)
            .start()
    }

    override fun onDenied() {
        //ShowToast.show(mContext, getStr(mContext, R.string.deny_permission))
    }

    override fun foreverDenied() {
        SnackBar.show(activity!!,
            false,
            getStr(activity!!, R.string.str_allow_storage),
            true,
            getStr(activity!!, R.string.enable),
            View.OnClickListener {})
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

            val fileUri = data?.data

            //You can get File object from intent
            val file: File = com.github.dhaval2404.imagepicker.ImagePicker.getFile(data)!!

            //You can also get File Path from intent
            profilePath = ImagePicker.getFilePath(data)!!
            img_bank_edit.setImageBitmap(null)
            img_bank_edit!!.setImageURI(fileUri)


        } else if (resultCode == com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR) {
            Toast.makeText(
                activity!!,
                com.github.dhaval2404.imagepicker.ImagePicker.getError(data),
                Toast.LENGTH_SHORT
            ).show()
        } else {

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

    // validating Number
    private fun isValidNumber(number: String?): Boolean {
        return if (number != null && number.length == 10) {
            true
        } else false
    }
}
