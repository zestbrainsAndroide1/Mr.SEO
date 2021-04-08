package com.zb.mrseo.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.model.DataModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.PostDetailModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_shop_details.*
import kotlinx.android.synthetic.main.activity_shop_details.cvApplyForHelp
import kotlinx.android.synthetic.main.activity_shop_details.img_product
import kotlinx.android.synthetic.main.activity_shop_details.tv_price
import kotlinx.android.synthetic.main.activity_shop_details.tv_product_desc

class ShopDetailsActivity : AppCompatActivity(), ApiResponseInterface {
    var postId: String = ""
    var mUserModel: LoginModel.Data? = null
    var categoryId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_details)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT

        }
        mUserModel = Prefs.getObject(
            this@ShopDetailsActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        setUi()
    }

    private fun setUi() {
        val extras = intent.extras
        if (extras != null) {
            postId = extras.getString("id", "")


        }
        cvEdit.setSafeOnClickListener {

            val intent = Intent(this@ShopDetailsActivity, EditContentActivity::class.java)
            intent.putExtra("id", postId)
            intent.putExtra("id1", categoryId)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        cvApplyForHelp.setSafeOnClickListener {
            start<ChatActivity>()
        }
        getPostDetail()
    }

    private fun getPostDetail() {
        if (isNetworkAvailable(this@ShopDetailsActivity)) {

            ApiRequest<Any>(
                activity = this@ShopDetailsActivity,
                objectType = ApiInitialize.initialize()
                    .getPostDetail(
                        postId,
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_POST_DETAIL,
                isShowProgressDialog = true,
                apiResponseInterface = this@ShopDetailsActivity
            )

        } else {
            SnackBar.show(
                this@ShopDetailsActivity,
                true,
                getStr(this@ShopDetailsActivity, R.string.str_network_error),
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

                        categoryId=response.data!!.categoryId.toString()
                        Glide.with(this@ShopDetailsActivity).load(response.data!!.image.toString())
                            .into(img_product)
                        tv_title_shop.text = response.data!!.title.toString()
                        tv_product_desc.text = response.data!!.description.toString()
                        tv_price.text = "$" + response.data!!.price.toString()

                    }
                    else -> ShowToast(response.message!!, this@ShopDetailsActivity)
                }
            }


        }
    }

}