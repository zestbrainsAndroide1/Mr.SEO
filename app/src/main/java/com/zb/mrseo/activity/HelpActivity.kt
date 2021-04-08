package com.zb.mrseo.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.SupportActivity
import com.zb.mrseo.adapter.HelperDetailAdapter
import com.zb.mrseo.adapter.HomeAdapter
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.interfaces.OnStatusClick
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.MyPostDetailModel
import com.zb.mrseo.model.PostModel
import com.zb.mrseo.model.StatusModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.activity_post_detail.tv_keyword_name
import kotlinx.android.synthetic.main.activity_post_detail.tv_post_title
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.cvApplyForHelp
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_profile.*

class HelpActivity : AppCompatActivity(), ApiResponseInterface, OnStatusClick {
    var postId: String = ""
    var mUserModel: LoginModel.Data? = null
    lateinit var helperDetailAdapter: HelperDetailAdapter
    var categoryId: String = ""
    lateinit var dialogCashSent: Dialog
    lateinit var dialogCashFinish: Dialog
    lateinit var dialogCashView: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        val extras = intent.extras
        if (extras != null) {
            postId = extras.getString("id", "")


        }
        setUi()
    }

    private fun setUi() {
        mUserModel = Prefs.getObject(
            this@HelpActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        helperDetailAdapter = HelperDetailAdapter(this@HelpActivity, this@HelpActivity,postId)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@HelpActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_helper?.layoutManager = linearLayoutManager1
        rv_helper?.adapter = helperDetailAdapter

        img_edit_content.setOnClickListener(View.OnClickListener {

            if (categoryId.equals("1")) {
                val intent = Intent(this@HelpActivity, EditShopBuyActivity::class.java)
                intent.putExtra("postId", postId)
                intent.putExtra("id", categoryId)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            } else if (categoryId.equals("2")) {
                val intent = Intent(this@HelpActivity, EditShopActivity::class.java)
                intent.putExtra("postId", postId)
                intent.putExtra("id", categoryId)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            } else if (categoryId.equals("3")) {
                val intent = Intent(this@HelpActivity, EditBlogActivity::class.java)
                intent.putExtra("postId", postId)
                intent.putExtra("id", categoryId)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            } else if (categoryId.equals("4")) {
                val intent = Intent(this@HelpActivity, EditCafeActivity::class.java)
                intent.putExtra("postId", postId)
                intent.putExtra("id", categoryId)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                finish()
            } else {

            }


        })

        getPostDetail()
    }

    private fun getPostDetail() {
        if (isNetworkAvailable(this@HelpActivity)) {

            ApiRequest<Any>(
                activity = this@HelpActivity,
                objectType = ApiInitialize.initialize()
                    .getMyPostDetail(

                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId
                    ),
                TYPE = WebConstant.GET_MY_POST_DETAIL,
                isShowProgressDialog = true,
                apiResponseInterface = this@HelpActivity
            )

        } else {
            SnackBar.show(
                this@HelpActivity,
                true,
                getStr(this@HelpActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_MY_POST_DETAIL -> {
                val response = apiResponseManager.response as MyPostDetailModel

                when (response.status) {
                    200 -> {

                        try {

                            categoryId = response.data!!.categoryId!!
                            if (response.data!!.categoryId!!.equals("1")) {
                                //1
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_name_content.text = response.data!!.platformName.toString()
                                tv_keyword_name_content.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name_content.text = response.data!!.name.toString()
                                tv_description_content.text = response.data!!.description.toString()
                                tv_remaining_points_content.text = response.data!!.point.toString()+"/"


                            } else if (response.data!!.categoryId!!.equals("2")) {
                                //1
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_name_content.text = response.data!!.platformName.toString()
                                tv_keyword_name_content.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name_content.text = response.data!!.name.toString()
                                tv_description_content.text = response.data!!.description.toString()
                                tv_remaining_points_content.text = response.data!!.point.toString()+"/"


                            } else if (response.data!!.categoryId!!.equals("3")) {
                                //3
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_keyword_name_content.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name_content.text = response.data!!.name.toString()
                                tv_description_content.text = response.data!!.description.toString()
                                tv_remaining_points_content.text = response.data!!.point.toString()+"/"
                                tv_shopping_mall_content.text = getString(R.string.blog_name)
                                tv_open_market_content.gone()
                                tv_name_content.gone()
                                line_first_content.gone()

                            } else if (response.data!!.categoryId!!.equals("4")) {
//4
                                tv_post_title.text = response.data!!.categoryName.toString()
                                tv_name_content.text = response.data!!.platformName.toString()
                                tv_keyword_name_content.text = response.data!!.keyword.toString()
                                tv_shopping_mall_name_content.text = response.data!!.name.toString()
                                tv_description_content.text = response.data!!.description.toString()
                                tv_remaining_points_content.text = response.data!!.point.toString()+"/"
                                tv_shopping_mall_content.text = getString(R.string.cafe_name)
                                tv_keyword_content.text = getString(R.string.cafe_url)

                            } else {

                            }

                            helperDetailAdapter.clear()
                            if (response.data!!.helper!!.size > 0) {
                                helperDetailAdapter.addAll(response.data!!.helper)
                            }

                            if (response.data!!.keyword!!.equals("")) {
                                tv_keyword_name_content.gone()
                                tv_keyword_content.gone()
                                line_second_keyword.gone()
                            }


                        } catch (e: Exception) {

                        }


                    }
                    else -> ShowToast(response.message!!, this@HelpActivity)
                }
            }

            WebConstant.CHANGE_STATUS -> {
                val response = apiResponseManager.response as StatusModel

                when (response.status) {
                    200 -> {

                        ShowToast(response.message!!, this@HelpActivity)
                        getPostDetail()
                    }
                    else -> ShowToast(response.message!!, this@HelpActivity)
                }
            }

        }
    }


    override fun onCashSentClick(position: Int, title: String, id: String) {
        cashSentDialog(title, id)

    }


    override fun onViewProofClick(position: Int, title: String, id: String) {
        cashViewDialog(title, id)
    }

    override fun onFinishedClick(position: Int, title: String, id: String) {
        cashFinishDialog(title, id)

    }

    override fun onBackPressed() {
        val intent = Intent(this@HelpActivity,MainActivity::class.java)
        intent.putExtra("show","content")
        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
       finish()
    }

    override fun onChatClick(position: Int, title: String, id: String) {
    }

    private fun cashSentDialog(id: String, status: String) {

        dialogCashSent = Dialog(this@HelpActivity)
        dialogCashSent.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogCashSent.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialogCashSent.setCancelable(false)
        dialogCashSent.setContentView(R.layout.dialog_cash_sent)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogCashSent.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialogCashSent.window!!.attributes = lp
        val llNo = dialogCashSent.findViewById(R.id.ll_no) as LinearLayout
        val cvYes = dialogCashSent.findViewById(R.id.cv_yes) as CardView


        llNo.setOnClickListener(View.OnClickListener {
            dialogCashSent.dismiss()
        })
        cvYes.setOnClickListener(View.OnClickListener {
            dialogCashSent.dismiss()
            changeStatus(id, status)
            HelperDetailAdapter.btnCashSent!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4EA3FE")));

            HelperDetailAdapter.btnViewProof!!.isEnabled=true
        })
        dialogCashSent.show()

    }

    private fun cashFinishDialog(id: String, status: String) {

        dialogCashFinish = Dialog(this@HelpActivity)
        dialogCashFinish.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogCashFinish.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialogCashFinish.setCancelable(false)
        dialogCashFinish.setContentView(R.layout.dialog_finish)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogCashFinish.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialogCashFinish.window!!.attributes = lp
        val llNo = dialogCashFinish.findViewById(R.id.ll_no) as LinearLayout
        val cvYes = dialogCashFinish.findViewById(R.id.cv_yes) as CardView


        llNo.setOnClickListener(View.OnClickListener {
            dialogCashFinish.dismiss()
        })
        cvYes.setOnClickListener(View.OnClickListener {
            dialogCashFinish.dismiss()

            changeStatus(id, status)
            HelperDetailAdapter.btnFinish!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4EA3FE")));

        })
        dialogCashFinish.show()

    }

    private fun cashViewDialog(id: String, status: String) {

        dialogCashView = Dialog(this@HelpActivity)
        dialogCashView.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogCashView.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialogCashView.setCancelable(false)
        dialogCashView.setContentView(R.layout.dialog_view)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialogCashView.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialogCashView.window!!.attributes = lp
        val llNo = dialogCashView.findViewById(R.id.ll_no) as LinearLayout
        val cvYes = dialogCashView.findViewById(R.id.cv_yes) as CardView


        llNo.setOnClickListener(View.OnClickListener {
            dialogCashView.dismiss()
        })
        cvYes.setOnClickListener(View.OnClickListener {
            dialogCashView.dismiss()

            changeStatus(id, status)
            HelperDetailAdapter.btnFinish!!.isEnabled=true
            HelperDetailAdapter.btnViewProof!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6CCF71")));

        })
        dialogCashView.show()

    }

    private fun changeStatus(id1: String, status1: String) {
        if (isNetworkAvailable(this@HelpActivity)) {

            ApiRequest<Any>(
                activity = this@HelpActivity,
                objectType = ApiInitialize.initialize()
                    .changeStatus(

                        "Bearer ".plus(mUserModel!!.token.toString()),
                        id1,
                        status1
                    ),
                TYPE = WebConstant.CHANGE_STATUS,
                isShowProgressDialog = true,
                apiResponseInterface = this@HelpActivity
            )

        } else {
            SnackBar.show(
                this@HelpActivity,
                true,
                getStr(this@HelpActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }


}