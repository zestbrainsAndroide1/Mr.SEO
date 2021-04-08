package com.zb.mrseo.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.adapter.PlatformAdapter
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.EditModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.MyPostDetailModel
import com.zb.mrseo.model.PlatformListModel
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.InputFilterMinMax
import kotlinx.android.synthetic.main.activity_edit_blog.*
import kotlinx.android.synthetic.main.activity_edit_cafe.*
import kotlinx.android.synthetic.main.activity_edit_shop.*
import kotlinx.android.synthetic.main.activity_edit_shop.cvAdd
import kotlinx.android.synthetic.main.activity_edit_shop_buy.*

class EditShopActivity : AppCompatActivity() , OnPlatformClick, ApiResponseInterface {
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
    var mUserModel: LoginModel.Data? = null
   var postId:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_shop)
        setUi()
    }
    private fun setUi() {
        val extras = intent.extras
        if (extras != null) {
            postId = extras.getString("postId", "")
            categoryId = extras.getString("id", "")
        }
        mUserModel = Prefs.getObject(
            this@EditShopActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        tv_total_point_shop.text = mUserModel!!.coin.toString()
        if(mUserModel!!.coin.toString().equals("0")){
            edtPoint.isEnabled=false
            tv_note_shop1.visible()
            ShowToast("Oops, You don't have sufficient coin",this@EditShopActivity)
        }else{
            edtPoint.filters = arrayOf<InputFilter>(InputFilterMinMax("1",  mUserModel!!.coin.toString()))

        }

        platformDialog()
        edtPlatForm.setSafeOnClickListener {
            dialog.show()
        }

        getPostDetail()

        cvAdd.setSafeOnClickListener {
            title = edtTitle.text.toString()
            mallName = edtMallName.text.toString()
            description = edtDesc.text.toString()
            registerPoint = edtPoint.text.toString()

            if (title.equals("")) {
                showToast(getString(R.string.title_validation1), this@EditShopActivity)

            } else if (mallName.equals("")) {

                showToast(getString(R.string.mall_name_validation),this@EditShopActivity)


            } else if (description.equals("")) {

                showToast(getString(R.string.desc_validation),this@EditShopActivity)


            }  else if (registerPoint.equals("")) {

                showToast(getString(R.string.point_validation), this@EditShopActivity)


            } else {
                editContent()
            }
        }

    }

    private fun platformDialog() {

        dialog = Dialog(this@EditShopActivity)
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

        platformAdapter = PlatformAdapter(this@EditShopActivity, this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@EditShopActivity,
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

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onPlatformCllick(position: Int, title: String, id: String) {
        platformId = id.toString()
        edtPlatForm.text = title.toString().toEditable()
        dialog.dismiss()
    }

    private fun getPlatformList() {
        if (isNetworkAvailable(this@EditShopActivity)) {

            ApiRequest<Any>(
                activity = this@EditShopActivity,
                objectType = ApiInitialize.initialize()
                    .getPlatformList(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_PLATFORM_LIST,
                isShowProgressDialog = false,
                apiResponseInterface =this@EditShopActivity
            )

        } else {
            SnackBar.show(
                this@EditShopActivity,
                true,
                getStr(this@EditShopActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_PLATFORM_LIST -> {
                val response = apiResponseManager.response as PlatformListModel

                when (response.status) {
                    200 -> {

                        platformAdapter.clear()
                        if (response.data!!.size > 0) {
                            platformAdapter.addAll(response.data!!)
                        } else {

                        }

                    }
                    else -> ShowToast(response.message!!, this@EditShopActivity)
                }
            }

            WebConstant.EDIT_SHOP_ONLINE -> {
                val response = apiResponseManager.response as EditModel

                when (response.status) {
                    200 -> {

                        ShowToast(response.message!!,this@EditShopActivity)
                        val intent = Intent(this@EditShopActivity, HelpActivity::class.java)
                        intent.putExtra("id",postId)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()

                    }
                    else -> ShowToast(response.message!!, this@EditShopActivity)
                }
            }

            WebConstant.GET_MY_POST_DETAIL -> {
                val response = apiResponseManager.response as MyPostDetailModel

                when (response.status) {
                    200 -> {

                        platformId = response.data!!.platformId.toString()

                        edtPlatForm.text = response.data!!.platformName.toString().toEditable()
                        edtTitle.text = response.data!!.title.toString().toEditable()
                        edtMallName.text = response.data!!.name.toString().toEditable()
                        edtDesc.text = response.data!!.description.toString().toEditable()
                        edtPoint.text = response.data!!.point.toString().toEditable()


                    }
                    else -> ShowToast(response.message!!, this@EditShopActivity)
                }
            }


        }
    }

    private fun editContent() {

        if (isNetworkAvailable(this@EditShopActivity)) {


            ApiRequest<Any>(
                activity = this@EditShopActivity,
                objectType = ApiInitialize.initialize()
                    .editContent(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId,
                        title,
                        platformId,
                        categoryId,
                        mallName,
                        description,
                        registerPoint
                    ),
                TYPE = WebConstant.EDIT_SHOP_ONLINE,
                isShowProgressDialog = true,
                apiResponseInterface = this@EditShopActivity
            )



        } else {
            SnackBar.show(
                this@EditShopActivity,
                true,
                getStr(this@EditShopActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }

    }

    private fun getPostDetail() {
        if (isNetworkAvailable(this@EditShopActivity)) {

            ApiRequest<Any>(
                activity = this@EditShopActivity,
                objectType = ApiInitialize.initialize()
                    .getMyPostDetail(

                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId
                    ),
                TYPE = WebConstant.GET_MY_POST_DETAIL,
                isShowProgressDialog = true,
                apiResponseInterface = this@EditShopActivity
            )

        } else {
            SnackBar.show(
                this@EditShopActivity,
                true,
                getStr(this@EditShopActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

}