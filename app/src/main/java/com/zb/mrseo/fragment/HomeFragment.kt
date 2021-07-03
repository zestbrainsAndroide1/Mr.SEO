package com.zb.mrseo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.activity.ForgotPwdActivity
import com.zb.mrseo.activity.TransactionActivity
import com.zb.mrseo.adapter.HomeAdapter
import com.zb.mrseo.model.AddContentModel
import com.zb.mrseo.model.HomeModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.PlatformListModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment(),ApiResponseInterface {
    lateinit var homeAdapter: HomeAdapter
    var mUserModel: LoginModel.Data? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUi()
    }

    private fun setUi() {
        mUserModel = Prefs.getObject(
            activity!!,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        tv_coin_count1.text=mUserModel!!.coin.toString()

        homeAdapter = HomeAdapter(activity!!)
        val linearLayoutManager1 =
            LinearLayoutManager(
                activity!!,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_home?.layoutManager = linearLayoutManager1
        rv_home?.adapter = homeAdapter

        fab.setSafeOnClickListener {
            val someFragment: Fragment = AddContentFragment()
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        }

        cv_coin_home.setSafeOnClickListener {
            val intent = Intent(activity!!, TransactionActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }
        getHomeData()
    }

    private fun getHomeData() {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .getHomeData(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_HOME_DATA,
                isShowProgressDialog = true,
                apiResponseInterface = this@HomeFragment
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

            WebConstant.GET_HOME_DATA -> {
                val response = apiResponseManager.response as HomeModel

                when (response.status) {
                    200 -> {
                        tv_coin_count1.text= response.coin.toString()
                        homeAdapter.clear()
                        if (response.data!!.size > 0) {
                            homeAdapter.addAll(response.data!!)
                        } else {

                        }
                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }
        }
    }


}