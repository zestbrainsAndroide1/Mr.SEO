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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.adapter.PlatformAdapter
import com.zb.mrseo.fragment.AddContentFragment
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.*
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.InputFilterMinMax
import com.zb.mrseo.utility.KeyboardUtils
import kotlinx.android.synthetic.main.activity_edit_cafe.*
import kotlinx.android.synthetic.main.activity_edit_shop.*
import kotlinx.android.synthetic.main.activity_edit_shop_buy.*
import kotlinx.android.synthetic.main.activity_edit_shop_buy.cvAdd
import kotlinx.android.synthetic.main.fragment_content_details.*
import kotlinx.android.synthetic.main.fragment_content_details.edtPoint

class EditShopBuyActivity : AppCompatActivity(), ApiResponseInterface, OnPlatformClick {
    var platformId: String = ""
    var categoryId: String = ""
    var shoppingMallName: String = ""
    var keyword: String = ""
    var description: String = ""
    var registerPoint: String = ""
    var mUserModel: LoginModel.Data? = null
    lateinit var dialog: Dialog
    lateinit var platformAdapter: PlatformAdapter
    var postId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_shop_buy)
        setUi()
    }

    private fun setUi() {
        val extras = intent.extras
        if (extras != null) {
            postId = extras.getString("postId", "")
            categoryId = extras.getString("id", "")
        }
        edtPointShop.filters = arrayOf<InputFilter>(InputFilterMinMax("1", "20"))

        mUserModel = Prefs.getObject(
            this@EditShopBuyActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        tv_total_points_shop_buy.text = mUserModel!!.coin.toString()
        if(mUserModel!!.coin.toString().equals("0")){
            edtPointShop.isEnabled=false
            tv_note_shop_buy.visible()
            ShowToast("Oops, You don't have sufficient coin",this@EditShopBuyActivity)
        }else{
            edtPointShop.filters = arrayOf<InputFilter>(InputFilterMinMax("1",  mUserModel!!.coin.toString()))

        }

        platformDialog()

        edtPlatFormShop.setSafeOnClickListener {
            dialog.show()
        }

        getPostDetail()



        cvAdd.setSafeOnClickListener {
            shoppingMallName = edtShoppingMallName.text.toString()
            description = edtDescShop.text.toString()
            keyword = edtKeywordShop.text.toString()
            registerPoint = edtPointShop.text.toString()

            if (shoppingMallName.equals("")) {
                showToast(getString(R.string.mall_validation1), this@EditShopBuyActivity)

            } else if (description.equals("")) {

                showToast(getString(R.string.desc_validation), this@EditShopBuyActivity)


            } else if (keyword.equals("")) {

                showToast(getString(R.string.keyword_validation), this@EditShopBuyActivity)


            } else if (registerPoint.equals("")) {

                showToast(getString(R.string.point_validation), this@EditShopBuyActivity)


            } else if (platformId.equals("")) {

                showToast(getString(R.string.platform_validation), this@EditShopBuyActivity)


            } else {
                editShop()
            }
        }

    }


    private fun editShop() {

        if (isNetworkAvailable(this@EditShopBuyActivity)) {

            ApiRequest<Any>(
                activity = this@EditShopBuyActivity,
                objectType = ApiInitialize.initialize()
                    .editShopBuy(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId,
                        platformId,
                        categoryId,
                        shoppingMallName,
                        description,
                        registerPoint,
                        keyword
                    ),
                TYPE = WebConstant.EDIT_SHOP_BUY,
                isShowProgressDialog = true,
                apiResponseInterface = this@EditShopBuyActivity
            )


        } else {
            SnackBar.show(
                this@EditShopBuyActivity,
                true,
                getStr(this@EditShopBuyActivity, R.string.str_network_error),
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
                    else -> ShowToast(response.message!!, this@EditShopBuyActivity)
                }
            }


            WebConstant.EDIT_SHOP_BUY -> {
                val response = apiResponseManager.response as EditModel

                when (response.status) {
                    200 -> {

                        ShowToast(response.message!!, this@EditShopBuyActivity)
                        val intent = Intent(this@EditShopBuyActivity, HelpActivity::class.java)
                        intent.putExtra("id", postId)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()

                    }
                    else -> ShowToast(response.message!!, this@EditShopBuyActivity)
                }
            }


            WebConstant.GET_MY_POST_DETAIL -> {
                val response = apiResponseManager.response as MyPostDetailModel

                when (response.status) {
                    200 -> {


                        platformId = response.data!!.platformId.toString()
                        edtPlatFormShop.text = response.data!!.platformName.toString().toEditable()
                        edtKeywordShop.text = response.data!!.keyword.toString().toEditable()
                        edtShoppingMallName.text = response.data!!.name.toString().toEditable()
                        edtDescShop.text = response.data!!.description.toString().toEditable()
                        edtPointShop.text = response.data!!.point.toString().toEditable()


                    }
                    else -> ShowToast(response.message!!, this@EditShopBuyActivity)
                }
            }


        }
    }

    private fun platformDialog() {

        dialog = Dialog(this@EditShopBuyActivity)
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

        platformAdapter = PlatformAdapter(this@EditShopBuyActivity, this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@EditShopBuyActivity,
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
        edtPlatFormShop.text = title.toString().toEditable()
        dialog.dismiss()
    }

    private fun getPlatformList() {
        if (isNetworkAvailable(this@EditShopBuyActivity)) {

            ApiRequest<Any>(
                activity = this@EditShopBuyActivity,
                objectType = ApiInitialize.initialize()
                    .getPlatformList(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_PLATFORM_LIST,
                isShowProgressDialog = false,
                apiResponseInterface = this@EditShopBuyActivity
            )

        } else {
            SnackBar.show(
                this@EditShopBuyActivity,
                true,
                getStr(this@EditShopBuyActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    private fun getPostDetail() {
        if (isNetworkAvailable(this@EditShopBuyActivity)) {

            ApiRequest<Any>(
                activity = this@EditShopBuyActivity,
                objectType = ApiInitialize.initialize()
                    .getMyPostDetail(

                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId
                    ),
                TYPE = WebConstant.GET_MY_POST_DETAIL,
                isShowProgressDialog = true,
                apiResponseInterface = this@EditShopBuyActivity
            )

        } else {
            SnackBar.show(
                this@EditShopBuyActivity,
                true,
                getStr(this@EditShopBuyActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

}