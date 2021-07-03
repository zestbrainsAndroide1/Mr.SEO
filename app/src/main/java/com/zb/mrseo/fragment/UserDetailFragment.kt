package com.zb.mrseo.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CompoundButton
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.SupportActivity
import com.zb.mrseo.activity.LoginActivity
import com.zb.mrseo.activity.NoticeActivity
import com.zb.mrseo.activity.TransactionActivity
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.LogoutModel
import com.zb.mrseo.model.UpdateStatusModel
import com.zb.mrseo.model.ViewProfileModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_user_detail.*
import kotlinx.android.synthetic.main.fragment_user_detail.img_logout
import kotlinx.android.synthetic.main.fragment_user_detail.tv_contact_us
import kotlinx.android.synthetic.main.fragment_user_detail.tv_user_name


class UserDetailFragment : Fragment(),ApiResponseInterface {
    lateinit var dialog1: Dialog
    var mUserModel: LoginModel.Data? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mUserModel = Prefs.getObject(
            activity!!,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        setUi()

    }
    private fun setUi() {
        getProfile()

        img_logout.setSafeOnClickListener {
            val bundle = Bundle()
            bundle.putString("id", "3")

            val someFragment: Fragment =ProfileFragment()
            someFragment!!.setArguments(bundle)

            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        }
        tv_contact_us.setOnClickListener(View.OnClickListener {

            val intent = Intent(activity!!, SupportActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
        tv_notice_board.setOnClickListener(View.OnClickListener {

            val intent = Intent(activity!!, NoticeActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
        ll_points.setOnClickListener(View.OnClickListener {

            val intent = Intent(activity!!, TransactionActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
        cl_profile_user.setOnClickListener(View.OnClickListener {

            val someFragment: Fragment = ProfileFragment()
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        })
        tv_logout.setOnClickListener(View.OnClickListener {

            logoutDialog()

        })


    }

    private fun logoutDialog() {

        dialog1 = Dialog(activity!!)
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
        val tvTitle = dialog1.findViewById(R.id.tv_title) as TextView
        tvTitle.text = "로그아웃 하시겠습니까?"



        tvNo.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
        })
        tvYes.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
            logout()
        })
        dialog1.show()

    }

    private fun getProfile() {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .getProfile(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_PROFILE,
                isShowProgressDialog = true,
                apiResponseInterface = this@UserDetailFragment
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

            WebConstant.GET_PROFILE -> {
                val response = apiResponseManager.response as ViewProfileModel

                when (response.status) {
                    200 -> {
                        try {

                            tv_user_name.text = response.data!!.name.toString()
                            tv_total_coins_user.text = response.data!!.coin.toString()
                            tv_coin_desc.text= response.data!!.coin.toString().toString()+" 포인트 사용 가능합니다."

                        } catch (e: Exception) {

                        }


                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }

            WebConstant.LOGOUT -> {
                val response = apiResponseManager.response as LogoutModel

                when (response.status) {
                    200 -> {
                        Prefs.removeValue(context = activity!!, key = AppConstant.USER_ID)

                        Prefs.clearAllData(activity!!)
                        val intent = Intent(activity!!, LoginActivity::class.java)
                        startActivity(intent)
                        activity!!.overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )
                        activity!!.finish()
                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }



        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun logout() {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .logout(
                        Prefs.getValue(activity!!, AppConstant.TOKEN, "")!!
                    ),
                TYPE = WebConstant.LOGOUT,
                isShowProgressDialog = true,
                apiResponseInterface = this@UserDetailFragment
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



}