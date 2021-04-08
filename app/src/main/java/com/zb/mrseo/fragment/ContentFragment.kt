package com.zb.mrseo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.SupportActivity
import com.zb.mrseo.activity.ChatActivity
import com.zb.mrseo.activity.ForgotPwdActivity
import com.zb.mrseo.activity.LoginActivity
import com.zb.mrseo.activity.TransactionActivity
import com.zb.mrseo.adapter.ContentListAdapter
import com.zb.mrseo.adapter.HelperAdapter
import com.zb.mrseo.adapter.HomeAdapter
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.*
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_option.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ContentFragment : Fragment(), ApiResponseInterface, OnPlatformClick {

    lateinit var contentListAdapter: ContentListAdapter
    var mUserModel: LoginModel.Data? = null
var show:String=""
    lateinit var helperAdapter:HelperAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            show = bundle.getString("show").toString()
        }
        setUi()
    }

    private fun setUi() {
        mUserModel = Prefs.getObject(
            activity!!,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        tv_coin_count_content.text=mUserModel!!.coin.toString()

        if(show.equals("help")){
            rl_my_content.visibility = View.GONE
            rl_helps.visibility = View.VISIBLE
            rv_content.gone()
            rv_help.visible()
            helperAdapter = HelperAdapter(activity!!,this)
            val linearLayoutManager1 =
                LinearLayoutManager(
                    activity!!,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rv_help?.layoutManager = linearLayoutManager1
            rv_help?.adapter = helperAdapter
            getHelpList()

        }
        cvHelps.setSafeOnClickListener {
            rl_my_content.visibility = View.GONE
            rl_helps.visibility = View.VISIBLE
            rv_content.gone()
            rv_help.visible()
            helperAdapter = HelperAdapter(activity!!,this)
            val linearLayoutManager1 =
                LinearLayoutManager(
                    activity!!,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            rv_help?.layoutManager = linearLayoutManager1
            rv_help?.adapter = helperAdapter
            getHelpList()

        }
        cvMyContens.setSafeOnClickListener {
            rl_my_content.visibility = View.VISIBLE
            rl_helps.visibility = View.GONE
            rv_content.visible()
            rv_help.gone()
            getMyPosts()
        }
        contentListAdapter = ContentListAdapter(activity!!,this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                activity!!,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_content?.layoutManager = linearLayoutManager1
        rv_content?.adapter = contentListAdapter
        cv_coin_content.setSafeOnClickListener {
            val intent = Intent(activity!!, TransactionActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

        getMyPosts()
    }

    private fun getMyPosts() {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .getMyPosts(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_MY_POSTS,
                isShowProgressDialog = true,
                apiResponseInterface = this@ContentFragment
            )

        } else {
            SnackBar.show(
                activity,
                true,
                getStr(activity!!, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    private fun getHelpList() {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .getMyHelperList(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_MY_HELPER_LIST,
                isShowProgressDialog = true,
                apiResponseInterface = this@ContentFragment
            )

        } else {
            SnackBar.show(
                activity,
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

            WebConstant.GET_MY_POSTS -> {
                val response = apiResponseManager.response as MyPostsModel

                when (response.status) {
                    200 -> {

                        contentListAdapter.clear()
                        if (response.data!!.size > 0) {
                            contentListAdapter.addAll(response.data!!)
                        } else {

                        }

                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }

            WebConstant.GET_MY_HELPER_LIST -> {
                val response = apiResponseManager.response as MyHelperModel

                when (response.status) {
                    200 -> {

                        helperAdapter.clear()
                        if (response.data!!.size > 0) {
                            helperAdapter.addAll(response.data!!)
                        } else {

                        }

                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }


            WebConstant.UPDATE_NOTIFICATION -> {
                val response = apiResponseManager.response as UpdateModel

                when (response.status) {
                    200 -> {
                        ShowToast(response.message!!, activity!!)
                       getMyPosts()

                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }

        }
    }

    override fun onPlatformCllick(position: Int, title: String, id: String) {
        updateNotification(id.toString(), title.toString())
    }

    private fun updateNotification(id: String, status: String) {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .updateNotification(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        status,
                        id
                    ),
                TYPE = WebConstant.UPDATE_NOTIFICATION,
                isShowProgressDialog = true,
                apiResponseInterface = this@ContentFragment
            )

        } else {
            SnackBar.show(
                activity,
                true,
                getStr(activity!!, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }


}