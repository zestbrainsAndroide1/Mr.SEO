package com.zb.mrseo.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.zb.mindme.permission.PermissionChecker
import com.zb.mindme.permission.PermissionStatus
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.adapter.PlatformAdapter
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.DataModel
import com.zb.mrseo.model.EditPostModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.PostDetailModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_edit_content2.*
import kotlinx.android.synthetic.main.activity_edit_content2.edtDesc
import kotlinx.android.synthetic.main.activity_edit_content2.edtPlatForm
import kotlinx.android.synthetic.main.activity_edit_content2.edtPoint
import kotlinx.android.synthetic.main.activity_edit_content2.edtPrice
import kotlinx.android.synthetic.main.activity_edit_content2.edtTitle
import kotlinx.android.synthetic.main.activity_edit_content2.img_back
import kotlinx.android.synthetic.main.activity_edit_content2.rlProfile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditContentActivity : AppCompatActivity(),ApiResponseInterface,OnPlatformClick ,PermissionStatus{
    lateinit var dialog: Dialog
    lateinit var platformAdapter: PlatformAdapter
    var platformId: String = ""
    var categoryId: String = ""
    var title: String = ""

    var mallName: String = ""
    var description: String = ""
    var price: String = ""
    var registerPoint: String = ""
    private var profilePath: String = ""
    var postId: String = ""
    var mUserModel: LoginModel.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_content2)
        setUi()
    }

    private fun setUi(){
        val extras = intent.extras
        if (extras != null) {
            postId = extras.getString("id", "")
            categoryId = extras.getString("id1", "")


        }
        mUserModel = Prefs.getObject(
            this@EditContentActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        platformDialog()
        edtPlatForm.setSafeOnClickListener {
            dialog.show()
        }
        img_back.setSafeOnClickListener {
            onBackPressed()
        }
        rlProfile.setOnClickListener(View.OnClickListener {
            PermissionChecker(
                this@EditContentActivity, this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
        })
        cvAddEdit.setSafeOnClickListener {
            title = edtTitle.text.toString()
            mallName = edtMallNameEdit.text.toString()
            description = edtDesc.text.toString()
            price = edtPrice.text.toString()
            registerPoint = edtPoint.text.toString()

            if (title.equals("")) {
                showToast(getString(R.string.title_validation1), this@EditContentActivity)

            } else if (mallName.equals("")) {

                showToast(getString(R.string.mall_name_validation), this@EditContentActivity)


            } else if (description.equals("")) {

                showToast(getString(R.string.desc_validation), this@EditContentActivity)


            } else if (price.equals("")) {

                showToast(getString(R.string.price_validation), this@EditContentActivity)


            } else if (registerPoint.equals("")) {

                showToast(getString(R.string.point_validation), this@EditContentActivity)


            } else {
                editContent()
            }
        }

        getPostDetail()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun getPostDetail() {
        if (isNetworkAvailable(this@EditContentActivity)) {

            ApiRequest<Any>(
                activity = this@EditContentActivity,
                objectType = ApiInitialize.initialize()
                    .getPostDetail(
                        postId,
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_POST_DETAIL,
                isShowProgressDialog = true,
                apiResponseInterface = this@EditContentActivity
            )

        } else {
            SnackBar.show(
                this@EditContentActivity,
                true,
                getStr(this@EditContentActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_POST_DETAIL -> {
                val response = apiResponseManager.response as PostDetailModel

                when (response.status) {
                    200 -> {

                        Glide.with(this@EditContentActivity).load(response.data!!.image.toString())
                            .into(img_edit_product)
                        edtPlatForm.text = response.data!!.platform!!.get(0).title.toString().toEditable()
                        edtTitle.text = response.data!!.title.toString().toEditable()
                        edtMallNameEdit.text =response.data!!.mallName.toString().toEditable()
                        edtDesc.text =response.data!!.description.toString().toEditable()
                        edtPrice.text =response.data!!.price.toString().toEditable()
                        edtPoint.text =response.data!!.registerPoint.toString().toEditable()

                    }
                    else -> ShowToast(response.message!!, this@EditContentActivity)
                }
            }

            WebConstant.EDIT_POST -> {
                val response = apiResponseManager.response as EditPostModel

                when (response.status) {
                    200 -> {

                        val intent = Intent(this@EditContentActivity, MainActivity::class.java)
                        intent.putExtra("show", "content")
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    }
                    else -> ShowToast(response.message!!, this@EditContentActivity)
                }
            }

        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onPlatformCllick(position: Int, title: String, id: String) {
        platformId = id.toString()
        edtPlatForm.text = title.toString().toEditable()
        dialog.dismiss()
    }

    private fun getPlatformList() {
        if (isNetworkAvailable(this@EditContentActivity)) {

            ApiRequest<Any>(
                activity = this@EditContentActivity,
                objectType = ApiInitialize.initialize()
                    .getPlatformList(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_PLATFORM_LIST,
                isShowProgressDialog = false,
                apiResponseInterface = this@EditContentActivity
            )

        } else {
            SnackBar.show(
                this@EditContentActivity,
                true,
                getStr(this@EditContentActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    private fun platformDialog() {

        dialog = Dialog(this@EditContentActivity)
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
        val rvPlatform = dialog.findViewById(R.id.rvPlatform) as RecyclerView

        platformAdapter = PlatformAdapter(this@EditContentActivity, this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@EditContentActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        rvPlatform?.layoutManager = linearLayoutManager1
        rvPlatform?.adapter = platformAdapter
        getPlatformList()
        imgClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })


    }

    private fun editContent() {

        if (isNetworkAvailable(this@EditContentActivity)) {

          


                val platformId1 = platformId.toRequestBody("text/plain".toMediaTypeOrNull())
                val categoryId1 = categoryId.toRequestBody("text/plain".toMediaTypeOrNull())
                val title1 = title.toRequestBody("text/plain".toMediaTypeOrNull())
                val mallName1 = mallName.toRequestBody("text/plain".toMediaTypeOrNull())
                val description1 = description.toRequestBody("text/plain".toMediaTypeOrNull())
                val price1 = price.toRequestBody("text/plain".toMediaTypeOrNull())
                val registerPoint1 = registerPoint.toRequestBody("text/plain".toMediaTypeOrNull())
                val postId1 = postId.toRequestBody("text/plain".toMediaTypeOrNull())


                val requestFile: RequestBody
                var profileImage: MultipartBody.Part? = null
               
                    requestFile = File(profilePath).asRequestBody("image/*".toMediaTypeOrNull())
                    profileImage =
                        MultipartBody.Part.createFormData(
                            "image",
                            "profile.jpg",
                            requestFile
                        )


                    ApiRequest<Any>(
                        activity = this@EditContentActivity,
                        objectType = ApiInitialize.initialize()
                            .editPost(
                                "Bearer ".plus(mUserModel!!.token.toString()),
                                title1,
                                platformId1,
                                categoryId1,
                                mallName1,
                                description1,
                                price1,
                                registerPoint1,
                                profileImage,
                                postId1

                            ),
                        TYPE = WebConstant.EDIT_POST,
                        isShowProgressDialog = true,
                        apiResponseInterface = this@EditContentActivity
                    )

                
            
        } else {
            SnackBar.show(
                this@EditContentActivity,
                true,
                getStr(this@EditContentActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }

    }

    override fun allGranted() {
        ImagePicker.with(this@EditContentActivity)
            .crop(16f, 16f)
            .compress(1024)
            //Final image size will be less than 1 MB(Optional)
            .start()
    }

    override fun onDenied() {
        //ShowToast.show(mContext, getStr(mContext, R.string.deny_permission))
    }

    override fun foreverDenied() {
        SnackBar.show(this@EditContentActivity,
            false,
            getStr(this@EditContentActivity, R.string.str_allow_storage),
            true,
            getStr(this@EditContentActivity, R.string.enable),
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
            profilePath = ImagePicker.getFilePath(data)!!

            img_edit_product.setImageBitmap(null)
            img_edit_product!!.setImageURI(fileUri)
        } else if (resultCode == com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR) {
            Toast.makeText(
                this@EditContentActivity,
                com.github.dhaval2404.imagepicker.ImagePicker.getError(data),
                Toast.LENGTH_SHORT
            ).show()
        } else {

        }


    }


}