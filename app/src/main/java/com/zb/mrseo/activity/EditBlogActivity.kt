package com.zb.mrseo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.model.EditModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.MyPostDetailModel
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.InputFilterMinMax
import kotlinx.android.synthetic.main.activity_edit_blog.*
import kotlinx.android.synthetic.main.activity_edit_blog.cvAddBlog
import kotlinx.android.synthetic.main.activity_edit_blog.edtBlogName
import kotlinx.android.synthetic.main.activity_edit_blog.edtDesc1
import kotlinx.android.synthetic.main.activity_edit_blog.edtKeyword
import kotlinx.android.synthetic.main.activity_edit_blog.edtPoint1
import kotlinx.android.synthetic.main.activity_edit_cafe.*
import kotlinx.android.synthetic.main.fragment_add_blog.*

class EditBlogActivity : AppCompatActivity(), ApiResponseInterface {
    var categoryId: String = ""
    var blogName: String = ""
    var keyword: String = ""
    var description: String = ""
    var registerPoint: String = ""
    var mUserModel: LoginModel.Data? = null
    var postId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_blog)
        setUi()
    }
    private fun setUi() {
        val extras = intent.extras
        if (extras != null) {
            postId = extras.getString("postId", "")
            categoryId = extras.getString("id", "")
        }

        mUserModel = Prefs.getObject(
            this@EditBlogActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        tv_total_points_blog1.text = mUserModel!!.coin.toString()
        if(mUserModel!!.coin.toString().equals("0")){
            edtPoint1.isEnabled=false
            tv_note_blog1.visible()
            ShowToast("Oops, You don't have sufficient coin",this@EditBlogActivity)
        }else{
            edtPoint1.filters = arrayOf<InputFilter>(InputFilterMinMax("1",  mUserModel!!.coin.toString()))

        }
        getPostDetail()

        cvAddBlog.setSafeOnClickListener {
            blogName = edtBlogName.text.toString()
            description = edtDesc1.text.toString()
            keyword = edtKeyword.text.toString()
            registerPoint = edtPoint1.text.toString()

            if (blogName.equals("")) {
                showToast(getString(R.string.blog_validation1), this@EditBlogActivity)

            } else if (description.equals("")) {

                showToast(getString(R.string.desc_validation), this@EditBlogActivity)


            } else if (keyword.equals("")) {

                showToast(getString(R.string.keyword_validation),this@EditBlogActivity)


            } else if (registerPoint.equals("")) {

                showToast(getString(R.string.point_validation), this@EditBlogActivity)


            } else {
                editBlog()
            }
        }

    }


    private fun editBlog() {

        if (isNetworkAvailable(this@EditBlogActivity)) {

            ApiRequest<Any>(
                activity =this@EditBlogActivity,
                objectType = ApiInitialize.initialize()
                    .editBlog(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId,
                        categoryId,
                        blogName,
                        description,
                        registerPoint,
                        keyword
                    ),
                TYPE = WebConstant.EDIT_BLOG,
                isShowProgressDialog = true,
                apiResponseInterface = this@EditBlogActivity
            )


        } else {
            SnackBar.show(
                this@EditBlogActivity,
                true,
                getStr(this@EditBlogActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }

    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {


            WebConstant.EDIT_BLOG -> {
                val response = apiResponseManager.response as EditModel

                when (response.status) {
                    200 -> {

                        ShowToast(response.message!!, this@EditBlogActivity)
                        val intent = Intent(this@EditBlogActivity, HelpActivity::class.java)
                        intent.putExtra("id",postId)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        finish()

                    }
                    else -> ShowToast(response.message!!, this@EditBlogActivity)
                }
            }


            WebConstant.GET_MY_POST_DETAIL -> {
                val response = apiResponseManager.response as MyPostDetailModel

                when (response.status) {
                    200 -> {


                        edtBlogName.text = response.data!!.name.toString().toEditable()
                        edtKeyword.text = response.data!!.keyword.toString().toEditable()
                        edtDesc1.text = response.data!!.description.toString().toEditable()
                        edtPoint1.text = response.data!!.point.toString().toEditable()


                    }
                    else -> ShowToast(response.message!!, this@EditBlogActivity)
                }
            }

        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun getPostDetail() {
        if (isNetworkAvailable(this@EditBlogActivity)) {

            ApiRequest<Any>(
                activity = this@EditBlogActivity,
                objectType = ApiInitialize.initialize()
                    .getMyPostDetail(

                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId
                    ),
                TYPE = WebConstant.GET_MY_POST_DETAIL,
                isShowProgressDialog = true,
                apiResponseInterface = this@EditBlogActivity
            )

        } else {
            SnackBar.show(
                this@EditBlogActivity,
                true,
                getStr(this@EditBlogActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }


}