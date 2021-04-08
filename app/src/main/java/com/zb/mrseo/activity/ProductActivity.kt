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
import com.zb.mrseo.model.*
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.cvLogIn
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProductActivity : AppCompatActivity(), ApiResponseInterface {
    var postId: String = ""
    var mUserModel: LoginModel.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT

        }
        val extras = intent.extras
        if (extras != null) {
           postId = extras.getString("id","")


        }
        setUi()
    }

    private fun setUi() {
        mUserModel = Prefs.getObject(
            this@ProductActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        cvApplyForHelp.setSafeOnClickListener {
            start<ChatActivity>()
        }
        getPostDetail()
    }

    private fun getPostDetail() {
        if (isNetworkAvailable(this@ProductActivity)) {

            ApiRequest<Any>(
                activity = this@ProductActivity,
                objectType = ApiInitialize.initialize()
                    .getPostDetail(
                        postId,
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_POST_DETAIL,
                isShowProgressDialog = true,
                apiResponseInterface = this@ProductActivity
            )

        } else {
            SnackBar.show(
                this@ProductActivity,
                true,
                getStr(this@ProductActivity, R.string.str_network_error),
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

                        Glide.with(this@ProductActivity).load(response.data!!.image.toString())
                            .into(img_product)
                        tv_product_title.text=response.data!!.title.toString()
                        tv_product_desc.text=response.data!!.description.toString()
                        tv_price.text="$"+response.data!!.price.toString()

                    }
                    else -> ShowToast(response.message!!, this@ProductActivity)
                }
            }


        }
    }

}