package com.zb.mrseo.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
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
import com.zb.mrseo.interfaces.OnDeleteClick
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.*
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_option.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ContentFragment : Fragment(), ApiResponseInterface, OnPlatformClick ,OnDeleteClick{

    lateinit var contentListAdapter: ContentListAdapter
    var mUserModel: LoginModel.Data? = null
var show:String=""
    lateinit var dialog1: Dialog

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
            img_no_data_content.gone()
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
            img_no_data_content.gone()

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
                    .getMyHelperList("Bearer ".plus(mUserModel!!.token.toString())),
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
                        tv_coin_count_content.text= response.coin.toString()
                        contentListAdapter.clear()
                        if (response.data!!.size > 0) {
                            contentListAdapter.addAll(response.data!!)
                            img_no_data_content.gone()

                        } else {
                            img_no_data_content.visible()

                        }

                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }

            WebConstant.DELETE_POST -> {
                val response = apiResponseManager.response as DeletePostModel

                when (response.status) {
                    200 -> {

                        getHelpList()
                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }


            WebConstant.GET_MY_HELPER_LIST -> {
                val response = apiResponseManager.response as MyHelperModel

                when (response.status) {
                    200 -> {
                        tv_coin_count_content.text= response.coin.toString()
                        helperAdapter.clear()
                        if (response.data!!.size > 0) {
                            helperAdapter.clear()
                            helperAdapter.addAll(response.data!!)
                            helperAdapter.notifyDataSetChanged()
                            img_no_data_content.gone()

                        } else {
                            img_no_data_content.visible()

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

    override fun OnDeleteClick(pos: Int, id: String) {
deletePostDialog(id)
    }

    private fun deletePost(id:String) {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .deletePost(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        id
                    ),
                TYPE = WebConstant.DELETE_POST,
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
    private fun deletePostDialog(id:String) {

        dialog1 = Dialog(requireActivity())
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialog1.setCancelable(false)
        dialog1.setContentView(R.layout.dialog_exit)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog1.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog1.window!!.attributes = lp
        val tvNo = dialog1.findViewById(R.id.tvNo) as TextView
        val tvYes = dialog1.findViewById(R.id.tvYes) as TextView
        val tvTitle=dialog1.findViewById(R.id.tv_title) as TextView

        tvTitle.text=getString(R.string.delete_desc)
        tvNo.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
        })
        tvYes.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()

            deletePost(id)
        })
        dialog1.show()

    }

}