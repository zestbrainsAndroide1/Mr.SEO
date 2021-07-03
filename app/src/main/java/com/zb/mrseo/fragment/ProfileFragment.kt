package com.zb.mrseo.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.SupportActivity
import com.zb.mrseo.activity.ChangePwdActivity
import com.zb.mrseo.activity.LoginActivity
import com.zb.mrseo.activity.ProductActivity
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.LogoutModel
import com.zb.mrseo.model.UpdateStatusModel
import com.zb.mrseo.model.ViewProfileModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(), ApiResponseInterface {

    lateinit var dialog1: Dialog
    var mUserModel: LoginModel.Data? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
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
        switch_notification.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(
                buttonView: CompoundButton?,
                isChecked: Boolean
            ) {
                if (isChecked) {
                    updateNotification("1")
                } else {
                    updateNotification("0")
                }
            }
        })
        img_logout.setSafeOnClickListener {
            logoutDialog()
        }
        tv_contact_us.setOnClickListener(View.OnClickListener {

            val intent = Intent(activity!!, SupportActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
        cl_password.setOnClickListener(View.OnClickListener {

            val intent = Intent(activity!!, ChangePwdActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
        img_edit.setOnClickListener(View.OnClickListener {

            val someFragment: Fragment = EditProfileFragment()
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        })

        img_back.setOnClickListener(View.OnClickListener {

            val someFragment: Fragment = UserDetailFragment()
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
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
                apiResponseInterface = this@ProfileFragment
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
                            tv_bank_name.text = response.data!!.bankName.toString()
                            tv_acc_no.text = response.data!!.accountNumber.toString()
                            tv_phone.text = response.data!!.mobile.toString()
                            tv_nick_name.text = response.data!!.nickName.toString()
                            tv_email.text = response.data!!.email.toString()

                            Glide.with(activity!!).load(response.data!!.bankImage.toString())
                                .into(img_bank)



                            tv_user_name.text = response.data!!.name.toString()

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
                        Prefs.removeValue(context = activity!!, key = AppConstant.ACCOUNT_DATA)

                        Prefs.clearAllData(requireActivity())
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

            WebConstant.UPDATE_STATUS -> {
                val response = apiResponseManager.response as UpdateStatusModel

                when (response.status) {
                    200 -> {
                        getProfile()

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
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.LOGOUT,
                isShowProgressDialog = true,
                apiResponseInterface = this@ProfileFragment
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

    private fun updateNotification(status: String) {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .updateStatus(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        status
                    ),
                TYPE = WebConstant.UPDATE_STATUS,
                isShowProgressDialog = false,
                apiResponseInterface = this@ProfileFragment
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