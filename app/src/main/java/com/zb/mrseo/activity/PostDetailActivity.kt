package com.zb.mrseo.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.model.*
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
    var helpModel : MyHelperModel? = null
    var mUserModel: LoginModel.Data? = null
    var mDetail: ApplyModel.Data? = null

    var receiverName = ""
    var receiverId = ""
    var threadId = ""
    var categoryId = ""
    var helpId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        val extras = intent.extras
        if (extras != null) {
            postId = extras.getString("id", "")
        }
        mDetail = intent.getSerializableExtra("detail") as? ApplyModel.Data
        setUi()
    }

    private fun setUi() {
        mUserModel = Prefs.getObject(
            this@PostDetailActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        /** 디테일 **/
        cvApplyForHelp2.setOnClickListener {
            val intent = Intent(this, HelpDetailActivity::class.java)
            intent.putExtra("id", helpId)
            startActivity(intent)
        }

        /** 채팅 **/
        cvApplyForHelp.setSafeOnClickListener {
            val intent = Intent(this@PostDetailActivity, ChatHistoryActivity::class.java)
            intent.putExtra("id", threadId)
            intent.putExtra("title", receiverName)
            intent.putExtra("receiverId", receiverId)
            intent.putExtra("type", "detail")

            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
        getPostDetail()

        img_back_post_detail.setSafeOnClickListener {
            onBackPressed()
        }
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

            WebConstant.GET_MY_HELPER_LIST -> {
                val response = apiResponseManager.response as MyHelperModel

                when (response.status) {
                    200 -> {
                        helpId = response.data!![0].helpId.toString()
                        getHelpDetail(helpId)
                    }
                    else -> ShowToast(response.message!!, this)
                }
            }

            WebConstant.GET_MY_HELPER_DETAIL -> {
                val response = apiResponseManager.response as MyHelpDetailModel
                tv_remaining_points1.text = response.data!![0].requiredPoint
            }

            WebConstant.GET_POST -> {
                val response = apiResponseManager.response as PostModel
                when (response.status) {
                    200 -> {
                        try {
                            threadId = response.data?.threadsId.toString()
                            receiverId = response.data?.userId.toString()
                            receiverName = response.data?.userName.toString()

                            /** 구매 품앗이 **/
                            if (response.data!!.categoryId!!.equals("1")) {
                                //1
                                categoryId = response.data!!.categoryId.toString()
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_name.text = response.data!!.platformName.toString()
                                tv_keyword_name.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name1.text = response.data!!.name.toString()
                                tv_description1.text = response.data!!.description.toString()
                                // tv_remaining_points1.text = "30"
                                tv_instruction.text = response.data!!.instructionMsg.toString()
                                //tv_sport_point.text = response.data!!.helperCount.toString()

                            /** 검색 풋앗이 **/
                            } else if (response.data!!.categoryId!!.equals("2")) {
                                //1
                                categoryId = response.data!!.categoryId.toString()
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_name.text = response.data!!.platformName.toString()
                                tv_keyword_name.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name1.text = response.data!!.name.toString()
                                tv_description1.text = response.data!!.description.toString()
                                // tv_remaining_points1.text = "5"
                                tv_instruction.text = response.data!!.instructionMsg.toString()
                                //tv_sport_point.text = response.data!!.helperCount.toString()

                            /** 블로그 **/
                            } else if (response.data!!.categoryId!!.equals("3")) {
                                //3
                                categoryId = response.data!!.categoryId.toString()
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_keyword_name.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name1.text = response.data!!.name.toString()
                                tv_description1.text = response.data!!.description.toString()
                                // tv_remaining_points1.text = "10"
                                tv_instruction.text = response.data!!.instructionMsg.toString()
                                //tv_sport_point.text = response.data!!.helperCount.toString()

                                tv_shopping_mall1.text = getString(R.string.blog_name)
                                tv_open_market1.gone()
                                tv_name.gone()
                                line_first1.gone()

                            /** 카페 **/
                            } else if (response.data!!.categoryId!!.equals("4")) {
                                categoryId = response.data!!.categoryId.toString()
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_name.text = response.data!!.url.toString()
                                tv_shopping_mall_name1.text = response.data!!.name.toString()
                                tv_description1.text = response.data!!.description.toString()
                                tv_instruction.text = response.data!!.instructionMsg.toString()
                                tv_shopping_mall1.text = getString(R.string.cafe_name)

                                tv_keyword1.text = getString(R.string.cafe_url)
                                tv_keyword_name.text = response.data!!.url.toString()

                                tv_open_market1.text = "제목"
                                tv_name.text = response.data!!.title.toString()



                            } else {

                            }


                        } catch (e: Exception) {

                        }
                        getHelpList()
                    }
                    else -> ShowToast(response.message!!, this@PostDetailActivity)
                }
            }

            WebConstant.APPLY_FOR_HELP -> {
                val response = apiResponseManager.response as ApplyModel

                when (response.status) {
                    200 -> {
                        val intent = Intent(this@PostDetailActivity, ChatHistoryActivity::class.java)
                        intent.putExtra("id", response.data!!.threadsId.toString())
                        intent.putExtra("title", response.data!!.receiverName.toString())
                        intent.putExtra("receiverId", response.data!!.receiverId.toString())
                        intent.putExtra("type", "detail")

                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    }
                    else -> ShowToast(response.message!!, this@PostDetailActivity)
                }
            }


        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@PostDetailActivity, MainActivity::class.java)
        intent.putExtra("show", "")
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        finish()
    }

    private fun getHelpList() {
        if (isNetworkAvailable(this)) {
            ApiRequest<Any>(
                activity = this,
                objectType = ApiInitialize.initialize()
                    .getMyHelperList("Bearer ".plus(mUserModel!!.token.toString())),
                TYPE = WebConstant.GET_MY_HELPER_LIST,
                isShowProgressDialog = true,
                apiResponseInterface = this
            )

        } else {
            SnackBar.show(
                this,
                true,
                getStr(this, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    private fun getHelpDetail(helpId : String) {
        if (isNetworkAvailable(this)) {

            ApiRequest<Any>(
                activity = this,
                objectType = ApiInitialize.initialize()
                    .getMyHelperDetail(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        helpId
                    ),
                TYPE = WebConstant.GET_MY_HELPER_DETAIL,
                isShowProgressDialog = true,
                apiResponseInterface = this
            )

        } else {
            SnackBar.show(
                this,
                true,
                getStr(this, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }
}