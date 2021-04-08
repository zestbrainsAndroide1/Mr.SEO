package com.zb.mrseo.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.adapter.AddNewImagesAdapter
import com.zb.mrseo.adapter.HomeAdapter
import com.zb.mrseo.adapter.ImageAdapter
import com.zb.mrseo.interfaces.OnItemSelectListener
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.MyHelpDetailModel
import com.zb.mrseo.model.PostModel
import com.zb.mrseo.model.UploadModel
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.AddProductImage
import kotlinx.android.synthetic.main.activity_help.*

import kotlinx.android.synthetic.main.activity_help_detail.*
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.activity_post_detail.tv_keyword_name
import kotlinx.android.synthetic.main.activity_post_detail.tv_name
import kotlinx.android.synthetic.main.activity_post_detail.tv_post_title
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.cvApplyForHelp
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class HelpDetailActivity : AppCompatActivity(), ApiResponseInterface, OnItemSelectListener {
    var helpId: String = ""
    var mUserModel: LoginModel.Data? = null
    private var imagesUrl: String? = null
    private var imageUrlList: ArrayList<String>? = null
    private var adapterAddProduct: AddNewImagesAdapter? = null
    private val IMAGE_DIRECTORY = "/bpi"
    private val GALLERY = 1
    internal var tempUri: Uri? = null

    private val CAMERA = 2
    private var addProductImages: ArrayList<AddProductImage>? = null
    var context: Context? = null
    lateinit var imgAdapter: ImageAdapter
    var upload: Int = 0
    var isUpload:Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_detail)
        val extras = intent.extras
        if (extras != null) {
            helpId = extras.getString("id", "")


        }
        context = this

        setUi()
    }

    private fun setUi() {
        mUserModel = Prefs.getObject(
            this@HelpDetailActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data
        cvSave.setSafeOnClickListener {
            upload()
        }
        addProductImages = ArrayList()
        imageUrlList = ArrayList()

        adapterAddProduct =
            addProductImages?.let { AddNewImagesAdapter(context!!, it, this) }
/*
        rvImg!!.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.VERTICAL)
*/
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@HelpDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        rvImg?.layoutManager = linearLayoutManager1
        rvImg!!.adapter = adapterAddProduct
        rvImg!!.setHasFixedSize(true)



        imgAdapter = ImageAdapter(this@HelpDetailActivity)
        val linearLayoutManager2 =
            LinearLayoutManager(
                this@HelpDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        rvImg1?.layoutManager = linearLayoutManager2
        rvImg1?.adapter = imgAdapter

        ll_img.setSafeOnClickListener {
            if (addProductImages!!.size >= upload) {
                showToast("You can upload max " + upload + " images", this@HelpDetailActivity)
            } else {
                requestMultiplePermissions()

            }
        }

        getHelpDetail()

    }

    private fun getHelpDetail() {
        if (isNetworkAvailable(this@HelpDetailActivity)) {

            ApiRequest<Any>(
                activity = this@HelpDetailActivity,
                objectType = ApiInitialize.initialize()
                    .getMyHelperDetail(

                        "Bearer ".plus(mUserModel!!.token.toString()),
                        helpId
                    ),
                TYPE = WebConstant.GET_MY_HELPER_DETAIL,
                isShowProgressDialog = true,
                apiResponseInterface = this@HelpDetailActivity
            )

        } else {
            SnackBar.show(
                this@HelpDetailActivity,
                true,
                getStr(this@HelpDetailActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_MY_HELPER_DETAIL -> {
                val response = apiResponseManager.response as MyHelpDetailModel

                when (response.status) {
                    200 -> {

                        try {

                            if (response.data!!.get(0)!!.categoryId!!.equals("1")) {
                                //1
                                upload = 2
                                tv_post_title_help.text =
                                    response.data!!.get(0).categoryName.toString()
                                tv_name_help.text = response.data!!.get(0).platformName.toString()
                                tv_keyword_name_help.text =
                                    response.data!!.get(0).keyword.toString()
                                tv_shopping_mall_name_help.text =
                                    response.data!!.get(0).name.toString()
                                tv_description_help1.text =
                                    response.data!!.get(0).description.toString()
                                tv_remaining_points_help.text =
                                    response.data!!.get(0).point.toString() + "/"
                                tv_total_points_help.text =
                                    response.data!!.get(0).helperCount.toString()


                            } else if (response.data!!.get(0)!!.categoryId!!.equals("2")) {
                                //1
                                upload = 1

                                tv_post_title_help.text =
                                    response.data!!.get(0).categoryName.toString()
                                tv_name_help.text = response.data!!.get(0).platformName.toString()
                                tv_keyword_name_help.text =
                                    response.data!!.get(0).keyword.toString()
                                tv_shopping_mall_name_help.text =
                                    response.data!!.get(0).name.toString()
                                tv_description_help1.text =
                                    response.data!!.get(0).description.toString()
                                tv_remaining_points_help.text =
                                    response.data!!.get(0).point.toString() + "/"
                                tv_total_points_help.text =
                                    response.data!!.get(0).helperCount.toString()

                            } else if (response.data!!.get(0)!!.categoryId!!.equals("3")) {
                                upload = 1

                                tv_post_title_help.text =
                                    response.data!!.get(0).categoryName.toString()
                                tv_name_help.text = response.data!!.get(0).platformName.toString()
                                tv_keyword_name_help.text =
                                    response.data!!.get(0).keyword.toString()
                                tv_shopping_mall_name_help.text =
                                    response.data!!.get(0).name.toString()
                                tv_description_help1.text =
                                    response.data!!.get(0).description.toString()
                                tv_remaining_points_help.text =
                                    response.data!!.get(0).point.toString() + "/"
                                tv_total_points_help.text =
                                    response.data!!.get(0).helperCount.toString()

                                tv_shopping_mall_help.text = getString(R.string.blog_name)
                                tv_open_market_help.gone()
                                tv_name_help.gone()
                                line_first_help.gone()

                            } else if (response.data!!.get(0)!!.categoryId!!.equals("4")) {
                                upload = 1

                                tv_post_title_help.text =
                                    response.data!!.get(0).categoryName.toString()
                                tv_name_help.text = response.data!!.get(0).platformName.toString()
                                tv_keyword_name_help.text =
                                    response.data!!.get(0).keyword.toString()
                                tv_shopping_mall_name_help.text =
                                    response.data!!.get(0).name.toString()
                                tv_description_help1.text =
                                    response.data!!.get(0).description.toString()
                                tv_remaining_points_help.text =
                                    response.data!!.get(0).point.toString() + "/"
                                tv_total_points_help.text =
                                    response.data!!.get(0).helperCount.toString()

                                tv_shopping_mall_help.text = getString(R.string.cafe_name)
                                tv_keyword_help.text = getString(R.string.cafe_url)

                            } else {

                            }
                            btn_status.text = response.data!!.get(0).status.toString()

                            if (response.data!!.get(0).keyword.toString().equals("")) {
                                tv_keyword_name_help.gone()
                                tv_keyword_help.gone()
                                line_second_help.gone()

                            } else {

                            }
                            if (response.data!!.get(0).proofImage!!.size > 0) {
                                rvImg.gone()
                                rvImg1.visible()
                                ll_img.gone()
                                imgAdapter.clear()
                                imgAdapter.addAll(response.data!!.get(0).proofImage!!)

                            } else {
                                rvImg1.gone()
                                rvImg.visible()
                                ll_img.visible()

                            }

                        } catch (e: Exception) {

                        }


                    }
                    else -> ShowToast(response.message!!, this@HelpDetailActivity)
                }
            }
            WebConstant.UPLOAD -> {
                val response = apiResponseManager.response as UploadModel

                when (response.status) {
                    200 -> {

                        if(upload.equals("2")){
                            isUpload=true
                            upload()
                        }
                        getHelpDetail()

                    }
                    else -> ShowToast(response.message!!, this@HelpDetailActivity)
                }
            }


        }
    }

    override fun OnItemSelectListener(pos: Int, title: String) {
        if (addProductImages!!.size >= upload) {

        } else {
            requestMultiplePermissions()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val fileUri = data?.data

            //You can get File object from intent
            val file: File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
            val filePath: String = ImagePicker.getFilePath(data)!!



            imageUrlList!!.add(ImagePicker.getFilePath(data).toString())
            imagesUrl = ImagePicker.getFilePath(data).toString()
            val addProductImage = AddProductImage()
            addProductImage.imageURI = imagesUrl
            addProductImage.uri = fileUri
            addProductImages!!.add(addProductImage)
            adapterAddProduct!!.notifyDataSetChanged()

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
/*
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
*/
        }
        /*if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        context!!.contentResolver,
                        contentURI)
                    imagesUrl = saveImage(bitmap) //String path = saveImage(bitmap);

                    //tempUri=saveImage(bitmap);
                    tempUri = getImageUri(context!!, Helper.getResizedBitmap(bitmap, 1000))

                    imageUrlList!!.add(tempUri.toString())

                    val addProductImage = AddProductImage()
                    addProductImage.imageURI = imagesUrl
                    addProductImage.uri = tempUri
                    addProductImages!!.add(addProductImage)
                    adapterAddProduct!!.notifyDataSetChanged()


                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("resulr", "failed " + e.message)
                }
            }
        } else if (requestCode == CAMERA) {


            if (data != null && data.extras != null) {

                val contentURI = data.data
                val thumbnail = data.extras!!["data"] as Bitmap?

                imagesUrl =
                    thumbnail?.let { saveImage(it) } //String path = saveImage(thumbnail);


                val mImageUri = data.extras!!["data"] as Bitmap?
                tempUri = getImageUri(context!!, Helper.getResizedBitmap(mImageUri!!, 1000))

                imageUrlList!!.add(tempUri.toString())

                val addProductImage = AddProductImage()
                addProductImage.imageURI = imagesUrl
                addProductImage.uri = tempUri
                addProductImages!!.add(addProductImage)
                adapterAddProduct!!.notifyDataSetChanged()
            }


        } else {

        }*/
    }

    private fun requestMultiplePermissions() {
        Dexter.withActivity(context as Activity?)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {

                        ImagePicker.with(this@HelpDetailActivity)
                            .crop(16f, 16f)
                            .compress(1024)
                            //Final image size will be less than 1 MB(Optional)
                            .start()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) { // show alert dialog navigating to Settings

                        Toast.makeText(
                            context,
                            "Please give permission first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<com.karumi.dexter.listener.PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener(object : PermissionRequestErrorListener {
                override fun onError(error: DexterError?) {
                    Toast.makeText(
                        context,
                        "Some Error! ",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
            .onSameThread()
            .check()
    }

    private fun upload() {
        if (isNetworkAvailable(context!!)) {
            val requestFile1: RequestBody
            var profileImage1: MultipartBody.Part? = null

            if(isUpload==true){

                requestFile1 = File(addProductImages!![1].imageURI).asRequestBody("image/*".toMediaTypeOrNull())
                profileImage1 =
                    MultipartBody.Part.createFormData(
                        "file",
                        "profile.jpg",
                        requestFile1
                    )


            }else{

                requestFile1 = File(addProductImages!![0].imageURI).asRequestBody("image/*".toMediaTypeOrNull())
                profileImage1 =
                    MultipartBody.Part.createFormData(
                        "file",
                        "profile.jpg",
                        requestFile1
                    )


            }
            val helpId1 = helpId.toRequestBody("text/plain".toMediaTypeOrNull())

            ApiRequest<Any>(
                activity = this@HelpDetailActivity,
                objectType = ApiInitialize.initialize()
                    .upload(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                       helpId1,
                       profileImage1
                    ),
                TYPE = WebConstant.UPLOAD,
                isShowProgressDialog = true,
                apiResponseInterface = this@HelpDetailActivity
            )

        } else {
            SnackBar.show(
                this@HelpDetailActivity,
                true,
                getStr(context!!, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this@HelpDetailActivity, MainActivity::class.java)
        intent.putExtra("show","content1")
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        finish()
    }
}