package com.zb.mrseo.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.iid.FirebaseInstanceId
import com.zb.mindme.permission.PermissionChecker
import com.zb.mindme.permission.PermissionStatus
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.adapter.*
import com.zb.mrseo.interfaces.OnCategoryClick
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.*
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_forgot_pwd.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.edtAccNo
import kotlinx.android.synthetic.main.dialog_terms.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.sufficientlysecure.htmltextview.HtmlTextView
import java.io.File
import java.util.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(), PermissionStatus, ApiResponseInterface,
    OnPlatformClick, OnCategoryClick {
    lateinit var dialog: Dialog
    lateinit var dialog1: Dialog

    lateinit var bankListAdapter: BankListAdapter
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var dialogCategory: Dialog
    lateinit var dialogSubCategory: Dialog
    var position = ""
    var position1 = ""
    var position2 = ""


    var categoryList: ArrayList<CategoryListModel.Data.Category>? =
        ArrayList<CategoryListModel.Data.Category>()

    var categoryIdList: java.util.ArrayList<String>? =
        java.util.ArrayList<String>()

    var subCatgoryIdList: java.util.ArrayList<String>? =
        java.util.ArrayList<String>()


    var mallCategory=""
    var url=""
    var mallSubCategory=""



    private var profilePath: String = ""
    lateinit var subCategoryAdapter: SubCategory1Adapter
    lateinit var subCategoryAdapter1: SubCategoryAdapter1
    lateinit var subCategoryAdapter2: SubCategoryAdapter2

    lateinit var dialogSubCategory1: Dialog
    lateinit var dialogSubCategory2: Dialog


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
    var type=""

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
        dialogCategory()
        getCategoryList()
/*
        tvCountry.text = getCountryCode()
*/
        cvSignUp.setSafeOnClickListener {
            email = edt_email.text.toString()
            password = edt_password.text.toString()
            name = edtName.text.toString()
            nickName = edtNickName.text.toString()
            mobile = edtPhone.text.toString().substring(1)
            bankName = edtBankName.text.toString()
            accNo = edtAccNo.text.toString()
            confirmPassword = edtConfirmPassword.text.toString()
            val commaSeperatedString = categoryIdList!!.joinToString(separator = ",") { it -> "\'${it}\'" }
            mallCategory = commaSeperatedString.replace("'", "")

            val commaSeperatedString1 = subCatgoryIdList!!.joinToString(separator = ",") { it -> "\'${it}\'" }
            mallSubCategory = commaSeperatedString1.replace("'", "")
url=edtWebsiteName.text.toString()
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

            }else if (edtWebsiteName.text.toString().equals("")) {

                showToast(getString(R.string.mall_link_validation), this@SignUpActivity)


            }else if (bankImagePath.toString().equals("")) {

                showToast(getString(R.string.img_validation), this@SignUpActivity)


            }else if (mallCategory.toString().equals("")) {

                showToast(getString(R.string.category_validation), this@SignUpActivity)


            } else if (mallSubCategory.toString().equals("")) {
                showToast(getString(R.string.sub_category_validation), this@SignUpActivity)

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
        edt_category.setSafeOnClickListener {
            type="category1"
            dialogCategory.show()
        }
        edt_category1.setSafeOnClickListener {
            type="category2"

            dialogCategory.show()
        }
        edt_category2.setSafeOnClickListener {
            type="category3"

            dialogCategory.show()
        }
        edt_sub_category.setSafeOnClickListener {
            type = "category1"


            if (type.equals("category1")) {
                if (!position.equals("")) {
                    if (categoryList!!.get(position.toInt()).subCategory!!.size > 0) {

                        dialogSubCategory(categoryList!!.get(position.toInt()).subCategory!!, "")

                    }
                }else {
                    dialogSubCategory.show()

                }
            }


        }
        edt_sub_category1.setSafeOnClickListener {
            type = "category2"
            if (type.equals("category2")) {

                if (!position1.equals("")) {
                    if (categoryList!!.get(position1.toInt()).subCategory!!.size > 0) {
                        dialogSubCategory1(categoryList!!.get(position1.toInt()).subCategory!!, "")

                    }
                }else {
                    dialogSubCategory1.show()

                }


            }
        }
        edt_sub_category2.setSafeOnClickListener {
            type = "category3"



            if (!position2.equals("")) {
                if (categoryList!!.get(position2.toInt()).subCategory!!.size > 0) {
                    /*subCategoryAdapter1.addAll(categoryList!!.get(position1.toInt()).subCategory!!)
                    subCategoryAdapter1.notifyDataSetChanged()*/
                    dialogSubCategory2(categoryList!!.get(position2.toInt()).subCategory!!, "")

                }
            }else{
                dialogSubCategory2.show()

            }


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
                                bankImagePath,
                                url,
                                mallCategory,
                                mallSubCategory
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

            WebConstant.GET_CATEGORY_LIST -> {
                val response = apiResponseManager.response as CategoryListModel

                when (response.status) {
                    200 -> {
                        categoryList!!.clear()
                        categoryAdapter.clear()
                        if (response.data!!.category!!.size > 0) {
                            categoryAdapter.addAll(response.data!!.category!!)
                            categoryList!!.addAll(response.data!!.category!!)

                        } else {

                        }


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

    private fun getCategoryList() {
        if (isNetworkAvailable(this@SignUpActivity)) {

            ApiRequest<Any>(
                activity = this@SignUpActivity,
                objectType = ApiInitialize.initialize()
                    .getCategoryList(),
                TYPE = WebConstant.GET_CATEGORY_LIST,
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

    private fun dialogCategory() {
        dialogCategory = Dialog(this@SignUpActivity)
        dialogCategory.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogCategory.setCancelable(true)
        dialogCategory.setContentView(R.layout.dialog_platform)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogCategory.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialogCategory.window!!.attributes = lp
        val imgClose = dialogCategory.findViewById(R.id.img_close) as ImageView
        val tvTitle = dialogCategory.findViewById(R.id.tv_title) as TextView

tvTitle.text="카테고리 선택"


        imgClose.setSafeOnClickListener {
            dialogCategory.dismiss()
        }
        val dialogRV = dialogCategory.findViewById(R.id.rvPlatform) as RecyclerView

        categoryAdapter = CategoryAdapter(this@SignUpActivity, this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@SignUpActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        dialogRV.layoutManager = linearLayoutManager1
        dialogRV.adapter = categoryAdapter


    }






    private fun dialogSubCategory(
        data: ArrayList<CategoryListModel.Data.Category.SubCategory>,
        status: String
    ) {
        dialogSubCategory = Dialog(this)
        dialogSubCategory.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSubCategory.setCancelable(true)
        dialogSubCategory.setContentView(R.layout.dialog_platform)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSubCategory.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialogSubCategory.window!!.attributes = lp
        val imgClose = dialogSubCategory.findViewById(R.id.img_close) as ImageView

        val tvTitle = dialogCategory.findViewById(R.id.tv_title) as TextView

        tvTitle.text = "하위 카테고리 선택"

        imgClose.setSafeOnClickListener {
            dialogSubCategory.dismiss()
        }
        val dialogRV = dialogSubCategory.findViewById(R.id.rvPlatform) as RecyclerView

        subCategoryAdapter = SubCategory1Adapter(this, data, this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        dialogRV.layoutManager = linearLayoutManager1
        dialogRV.adapter = subCategoryAdapter


        if (status.equals("")) {
            dialogSubCategory.show()

        } else {

        }
    }


    private fun dialogSubCategory1(
        data: ArrayList<CategoryListModel.Data.Category.SubCategory>,
        status: String
    ) {
        dialogSubCategory1 = Dialog(this)
        dialogSubCategory1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSubCategory1.setCancelable(true)
        dialogSubCategory1.setContentView(R.layout.dialog_platform)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSubCategory1.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialogSubCategory1.window!!.attributes = lp
        val imgClose = dialogSubCategory1.findViewById(R.id.img_close) as ImageView

        val tvTitle = dialogSubCategory1.findViewById(R.id.tv_title) as TextView

        tvTitle.text = "하위 카테고리 선택"

        imgClose.setSafeOnClickListener {
            dialogSubCategory1.dismiss()
        }
        val dialogRV = dialogSubCategory1.findViewById(R.id.rvPlatform) as RecyclerView


        subCategoryAdapter1 = SubCategoryAdapter1(this, data, this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        dialogRV.layoutManager = linearLayoutManager1
        dialogRV.adapter = subCategoryAdapter1



        if (status.equals("")) {
            dialogSubCategory1.show()

        } else {

        }
    }


    private fun dialogSubCategory2(
        data: ArrayList<CategoryListModel.Data.Category.SubCategory>,
        status: String
    ) {
        dialogSubCategory2 = Dialog(this)
        dialogSubCategory2.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogSubCategory2.setCancelable(true)
        dialogSubCategory2.setContentView(R.layout.dialog_platform)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogSubCategory2.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialogSubCategory2.window!!.attributes = lp
        val imgClose = dialogSubCategory2.findViewById(R.id.img_close) as ImageView

        val tvTitle = dialogSubCategory2.findViewById(R.id.tv_title) as TextView

        tvTitle.text = "하위 카테고리 선택"

        imgClose.setSafeOnClickListener {
            dialogSubCategory2.dismiss()
        }
        val dialogRV = dialogSubCategory2.findViewById(R.id.rvPlatform) as RecyclerView


        subCategoryAdapter2 = SubCategoryAdapter2(this, data, this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        dialogRV.layoutManager = linearLayoutManager1
        dialogRV.adapter = subCategoryAdapter2



        if (status.equals("")) {
            dialogSubCategory2.show()

        } else {

        }
    }


    override fun onCategoryClick(listPosition: Int, name: String, id: String) {


        if (type.equals("category1")) {
            edt_sub_category.isEnabled=true
            position=listPosition.toString()
try{
    if(!edt_category.text.toString().equals(name) && !edt_category.text.toString().equals("")){
        categoryIdList!!.set(0,id)
        try{
            if(subCatgoryIdList?.size==1){
                subCatgoryIdList!!.removeAt(0)

            }else if(subCatgoryIdList?.size==2){
                subCatgoryIdList!!.removeAt(1)

            }else if(subCatgoryIdList?.size==3){
                subCatgoryIdList!!.removeAt(2)

            }else{

            }

        }catch (e:java.lang.Exception){

        }
        edt_sub_category.text?.clear()

    }else if(edt_category.text.toString().equals(name)){

    }else{
        categoryIdList!!.add(id)


    }


}catch (e:Exception){

}

            if (listPosition.equals("")) {
                dialogSubCategory(categoryList!!.get(position.toInt()).subCategory!!, "")

            } else {
                position = ""

                if (categoryList!!.get(listPosition).subCategory!!.size > 0) {
                    dialogSubCategory(categoryList!!.get(listPosition).subCategory!!, "true")
                } else {
                }
            }

            edt_category.setText(name)
        } else if (type.equals("category2")) {
            edt_sub_category1.isEnabled=true

            position1=listPosition.toString()

            try{
                if(!edt_category1.text.toString().equals(name) && !edt_category1.text.toString().equals("")){
                    categoryIdList!!.set(1,id)
                    try{
                        if(subCatgoryIdList?.size==1){
                            subCatgoryIdList!!.removeAt(0)

                        }else if(subCatgoryIdList?.size==2){
                            subCatgoryIdList!!.removeAt(1)

                        }else if(subCatgoryIdList?.size==3){
                            subCatgoryIdList!!.removeAt(2)

                        }else{

                        }

                    }catch (e:java.lang.Exception){

                    }
                    edt_sub_category1.text?.clear()

                }else if(edt_category1.text.toString().equals(name)){

                }else{
                    categoryIdList!!.add(id)


                }
            }catch (e:java.lang.Exception){

            }



            if (listPosition.equals("")) {


                dialogSubCategory1(categoryList!!.get(position1.toInt()).subCategory!!, "")
            } else {
                position1 = ""
                dialogSubCategory1(categoryList!!.get(listPosition).subCategory!!, "true")

            }

            edt_category1.setText(name)

        } else if (type.equals("category3")) {
            edt_sub_category2.isEnabled=true

            position2=listPosition.toString()

            try {
                if(!edt_category2.text.toString().equals(name) && !edt_category2.text.toString().equals("")){
                    categoryIdList!!.set(2,id)
                    try{
                        if(subCatgoryIdList?.size==1){
                            subCatgoryIdList!!.removeAt(0)

                        }else if(subCatgoryIdList?.size==2){
                            subCatgoryIdList!!.removeAt(1)

                        }else if(subCatgoryIdList?.size==3){
                            subCatgoryIdList!!.removeAt(2)

                        }else{

                        }

                    }catch (e:java.lang.Exception){

                    }
                    edt_sub_category2.text?.clear()

                }else if(edt_category2.text.toString().equals(name)){

                }else{
                    categoryIdList!!.add(id)


                }
            }catch (e:java.lang.Exception){

            }

            if (listPosition.equals("")) {
                dialogSubCategory2(categoryList!!.get(position2.toInt()).subCategory!!, "")

            } else {
                position2 = ""
                dialogSubCategory2(categoryList!!.get(listPosition).subCategory!!, "true")

            }
            edt_category2.setText(name)

        } else {

        }
        dialogCategory.dismiss()
    }

    override fun onSubCategoryClick(listPosition: Int, name: String, id: String) {

        subCatgoryIdList!!.add(0,id)

        edt_sub_category.setText(name)

        dialogSubCategory.dismiss()
    }

    override fun onSubCategoryClick1(listPosition: Int, name: String, id: String) {
        subCatgoryIdList!!.add(1,id)

        edt_sub_category1.setText(name)
        dialogSubCategory1.dismiss()

    }

    override fun onSubCategoryClick2(listPosition: Int, name: String, id: String) {
        subCatgoryIdList!!.add(2,id)

        edt_sub_category2.setText(name)
        dialogSubCategory2.dismiss()

    }


}