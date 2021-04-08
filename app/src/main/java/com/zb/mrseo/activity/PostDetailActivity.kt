package com.zb.mrseo.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.model.ApplyModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.PostDetailModel
import com.zb.mrseo.model.PostModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.activity_post_detail.tv_keyword_name
import kotlinx.android.synthetic.main.activity_post_detail.tv_name
import kotlinx.android.synthetic.main.activity_post_detail.tv_post_title
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.cvApplyForHelp
import kotlinx.android.synthetic.main.activity_product.tv_product_title

class PostDetailActivity : AppCompatActivity(), ApiResponseInterface {

    var postId: String = ""
    var mUserModel: LoginModel.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        val extras = intent.extras
        if (extras != null) {
            postId = extras.getString("id", "")


        }
        setUi()
    }

    private fun setUi() {
        mUserModel = Prefs.getObject(
            this@PostDetailActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        cvApplyForHelp.setSafeOnClickListener {
            applyForHelp()
        }
        getPostDetail()
    }

    private fun getPostDetail() {
        if (isNetworkAvailable(this@PostDetailActivity)) {

            ApiRequest<Any>(
                activity = this@PostDetailActivity,
                objectType = ApiInitialize.initialize()
                    .getPost(

                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId
                    ),
                TYPE = WebConstant.GET_POST,
                isShowProgressDialog = true,
                apiResponseInterface = this@PostDetailActivity
            )

        } else {
            SnackBar.show(
                this@PostDetailActivity,
                true,
                getStr(this@PostDetailActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    private fun applyForHelp() {
        if (isNetworkAvailable(this@PostDetailActivity)) {

            ApiRequest<Any>(
                activity = this@PostDetailActivity,
                objectType = ApiInitialize.initialize()
                    .applyForHelp(

                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId
                    ),
                TYPE = WebConstant.APPLY_FOR_HELP,
                isShowProgressDialog = true,
                apiResponseInterface = this@PostDetailActivity
            )

        } else {
            SnackBar.show(
                this@PostDetailActivity,
                true,
                getStr(this@PostDetailActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }


    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_POST -> {
                val response = apiResponseManager.response as PostModel

                when (response.status) {
                    200 -> {

                        try {

                            if (response.data!!.categoryId!!.equals("1")) {
                                //1
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_name.text = response.data!!.platformName.toString()
                                tv_keyword_name.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name1.text = response.data!!.name.toString()
                                tv_description1.text = response.data!!.description.toString()
                                tv_remaining_points1.text = response.data!!.point.toString()


                            } else if (response.data!!.categoryId!!.equals("2")) {
                                //1
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_name.text = response.data!!.platformName.toString()
                                tv_keyword_name.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name1.text = response.data!!.name.toString()
                                tv_description1.text = response.data!!.description.toString()
                                tv_remaining_points1.text = response.data!!.point.toString()


                            } else if (response.data!!.categoryId!!.equals("3")) {
                                //3
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_keyword_name.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name1.text = response.data!!.name.toString()
                                tv_description1.text = response.data!!.description.toString()
                                tv_remaining_points1.text = response.data!!.point.toString()
                                tv_shopping_mall1.text = getString(R.string.blog_name)
                                tv_open_market1.gone()
                                tv_name.gone()
                                line_first1.gone()

                            } else if (response.data!!.categoryId!!.equals("4")) {
//4
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_name.text = response.data!!.platformName.toString()
                                tv_keyword_name.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name1.text = response.data!!.name.toString()
                                tv_description1.text = response.data!!.description.toString()
                                tv_remaining_points1.text = response.data!!.point.toString()
                                tv_shopping_mall1.text = getString(R.string.cafe_name)
                                tv_keyword1.text = getString(R.string.cafe_url)

                            } else {

                            }




                        } catch (e: Exception) {

                        }


                    }
                    else -> ShowToast(response.message!!, this@PostDetailActivity)
                }
            }
            WebConstant.APPLY_FOR_HELP -> {
                val response = apiResponseManager.response as ApplyModel

                when (response.status) {
                    200 -> {


                        val intent = Intent(this@PostDetailActivity, ChatHistoryActivity::class.java)
                        intent.putExtra("id",response.data!!.threadsId.toString())
                        intent.putExtra("title",response.data!!.receiverName.toString())
                        intent.putExtra("receiverId",response.data!!.receiverId.toString())
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)


                    }
                    else -> ShowToast(response.message!!, this@PostDetailActivity)
                }
            }


        }
    }

}