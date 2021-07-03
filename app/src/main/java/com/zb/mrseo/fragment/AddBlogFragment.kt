package com.zb.mrseo.fragment

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.model.AddBlogModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.InputFilterMinMax
import com.zb.mrseo.utility.KeyboardUtils
import kotlinx.android.synthetic.main.fragment_add_blog.*
import kotlinx.android.synthetic.main.fragment_content_details.*


class AddBlogFragment : Fragment(), ApiResponseInterface {

    var categoryId: String = ""
    var blogName: String = ""
    var keyword: String = ""
    var description: String = ""
    var registerPoint: String = ""
    var mUserModel: LoginModel.Data? = null
    var point=""
    var coin = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_blog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            categoryId = bundle.getString("id").toString()
            point = bundle.getString("point").toString()
            coin = bundle.getString("coin").toString()
        }

        setUi()
    }

    private fun setUi() {

        mUserModel = Prefs.getObject(
            activity!!,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data



        edtHelpersBlog.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                try{
                    var helper=edtHelpersBlog.text.toString()
                    var total= helper.toInt()*point.toInt()
                    edtPoint1.setText(total.toString())
                    if(total > coin.toInt()){
                        edtPoint1.isEnabled=false
                        tv_note_blog.visible()
                        ShowToast("Oops, You don't have sufficient coin",requireActivity())
                    }else{
                        tv_note_blog.gone()
                    }
                }catch(e:Exception){

                }
            }

            override fun afterTextChanged(s: Editable) {


            }
        })




        try {
            KeyboardUtils.addKeyboardToggleListener(activity!!, object :
                KeyboardUtils.SoftKeyboardToggleListener {
                override fun onToggleSoftKeyboard(isVisible: Boolean) {
                    if (isVisible) {
                        MainActivity.cvMain!!.visibility = View.GONE

                    } else {
                        MainActivity.cvMain!!.visibility = View.VISIBLE
                    }
                }
            })
        } catch (e: Exception) {

        }


        cvAddBlog.setSafeOnClickListener {
            blogName = edtBlogName.text.toString()
            description = edtDesc1.text.toString()
            keyword = edtKeyword.text.toString()
            registerPoint = edtHelpersBlog.text.toString()

            if (blogName.equals("")) {
                showToast(getString(R.string.blog_validation1), activity!!)

            } else if (description.equals("")) {

                showToast(getString(R.string.desc_validation), activity!!)


            } else if (keyword.equals("")) {

                showToast(getString(R.string.keyword_validation), activity!!)


            } else if (registerPoint.equals("")) {

                showToast(getString(R.string.helper_validation), activity!!)


            } else {
                addBlog()
            }
        }

    }

    private fun addBlog() {
        if (isNetworkAvailable(activity!!)) {
            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .addBlog(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        categoryId,
                        blogName,
                        description,
                        registerPoint,
                        keyword
                    ),
                TYPE = WebConstant.ADD_BLOG,
                isShowProgressDialog = true,
                apiResponseInterface = this@AddBlogFragment
            )
        } else {
            SnackBar.show(
                activity!!,
                true,
                getStr(activity!!, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }

    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.ADD_BLOG -> {
                val response = apiResponseManager.response as AddBlogModel

                when (response.status) {
                    200 -> {
                        ShowToast(response.message!!, activity!!)
                        val someFragment: Fragment = AddContentFragment()
                        val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                        transaction.replace(R.id.frmContainer, someFragment)
                        transaction.addToBackStack(null) // if written, this transaction will be added to backstack
                        transaction.commit()
                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }

        }
    }


}