package com.zb.mrseo.activity

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter

import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.adapter.PlatformAdapter

import com.zb.mrseo.model.EditModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.MyPostDetailModel
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.InputFilterMinMax
import kotlinx.android.synthetic.main.activity_edit_cafe.*
import kotlinx.android.synthetic.main.activity_edit_cafe.cvAddCafe
import kotlinx.android.synthetic.main.activity_edit_cafe.edtCafeName
import kotlinx.android.synthetic.main.activity_edit_cafe.edtCafeUrl
import kotlinx.android.synthetic.main.activity_edit_cafe.edtDescCafe
import kotlinx.android.synthetic.main.activity_edit_cafe.edtPointCafe
import kotlinx.android.synthetic.main.activity_edit_cafe.edtTitleCafe
import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.activity_post_detail.tv_post_title
import kotlinx.android.synthetic.main.fragment_add_cafe.*

class EditCafeActivity : AppCompatActivity(), ApiResponseInterface {
    var platformId: String = ""
    var categoryId: String = ""
    var cafeName: String = ""
    var url: String = ""
    var description: String = ""
    var registerPoint: String = ""
    var mUserModel: LoginModel.Data? = null
    lateinit var dialog: Dialog
    lateinit var platformAdapter: PlatformAdapter
    var postId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cafe)
        setUi()
    }

    private fun setUi() {
        val extras = intent.extras
        if (extras != null) {
            postId = extras.getString("postId", "")
            categoryId = extras.getString("id", "")
        }


        mUserModel = Prefs.getObject(
            this@EditCafeActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        tv_total_points_cafe2.text = mUserModel!!.coin.toString()
        if (mUserModel!!.coin.toString().equals("0")) {
            edtPointCafe.isEnabled = false
            tv_note_cafe2.visible()
            ShowToast("Oops, You don't have sufficient coin", this@EditCafeActivity)
        } else {
            edtPointCafe.filters =
                arrayOf<InputFilter>(InputFilterMinMax("1", mUserModel!!.coin.toString()))

        }
        getPostDetail()

        cvAddCafe.setSafeOnClickListener {
            cafeName = edtCafeName.text.toString()
            description = edtDescCafe.text.toString()
            url = edtCafeUrl.text.toString()
            registerPoint = edtPointCafe.text.toString()

            if (cafeName.equals("")) {
                showToast(getString(R.string.cafe_validation1), this@EditCafeActivity)

            } else if (description.equals("")) {

                showToast(getString(R.string.desc_validation), this@EditCafeActivity)


            } else if (url.equals("")) {

                showToast(getString(R.string.url_validation), this@EditCafeActivity)


            } else if (registerPoint.equals("")) {

                showToast(getString(R.string.point_validation), this@EditCafeActivity)


            } else {
                editCafe()
            }
        }

    }


    private fun editCafe() {

        if (isNetworkAvailable(this@EditCafeActivity)) {

            ApiRequest<Any>(
                activity = this@EditCafeActivity,
                objectType = ApiInitialize.initialize()
                    .editCafe(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId,
                        edtTitleCafe.text.toString(),
                        categoryId,
                        cafeName,
                        description,
                        registerPoint,
                        url
                    ),
                TYPE = WebConstant.EDIT_CAFE,
                isShowProgressDialog = true,
                apiResponseInterface = this@EditCafeActivity
            )


        } else {
            SnackBar.show(
                this@EditCafeActivity,
                true,
                getStr(this@EditCafeActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }

    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {


            WebConstant.EDIT_CAFE -> {
                val response = apiResponseManager.response as EditModel

                when (response.status) {
                    200 -> {

                        ShowToast(response.message!!, this@EditCafeActivity)


                        val intent = Intent(this@EditCafeActivity, HelpActivity::class.java)
                        intent.putExtra("id", postId)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()
                    }
                    else -> ShowToast(response.message!!, this@EditCafeActivity)
                }
            }

            WebConstant.GET_MY_POST_DETAIL -> {
                val response = apiResponseManager.response as MyPostDetailModel

                when (response.status) {
                    200 -> {


                        edtTitleCafe.text = response.data!!.title.toString().toEditable()
                        edtCafeName.text = response.data!!.name.toString().toEditable()
                        edtCafeUrl.text = response.data!!.url.toString().toEditable()
                        edtDescCafe.text = response.data!!.description.toString().toEditable()
                        edtPointCafe.text = response.data!!.point.toString().toEditable()


                    }
                    else -> ShowToast(response.message!!, this@EditCafeActivity)
                }
            }


        }
    }


    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun getPostDetail() {
        if (isNetworkAvailable(this@EditCafeActivity)) {

            ApiRequest<Any>(
                activity = this@EditCafeActivity,
                objectType = ApiInitialize.initialize()
                    .getMyPostDetail(

                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId
                    ),
                TYPE = WebConstant.GET_MY_POST_DETAIL,
                isShowProgressDialog = true,
                apiResponseInterface = this@EditCafeActivity
            )

        } else {
            SnackBar.show(
                this@EditCafeActivity,
                true,
                getStr(this@EditCafeActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }


}