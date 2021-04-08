package com.zb.mrseo.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.adapter.PlatformAdapter
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.AddCafeModel
import com.zb.mrseo.model.AddShopModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.PlatformListModel
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.InputFilterMinMax
import com.zb.mrseo.utility.KeyboardUtils
import kotlinx.android.synthetic.main.fragment_add_blog.*
import kotlinx.android.synthetic.main.fragment_add_cafe.*
import kotlinx.android.synthetic.main.fragment_add_shop.*


class AddShopFragment : Fragment(), ApiResponseInterface, OnPlatformClick {

    var platformId: String = ""
    var categoryId: String = ""
    var shoppingMallName: String = ""
    var keyword: String = ""
    var description: String = ""
    var registerPoint: String = ""
    var mUserModel: LoginModel.Data? = null
    lateinit var dialog: Dialog
    lateinit var platformAdapter: PlatformAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_shop, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            categoryId = bundle.getString("id").toString()
        }

        setUi()
    }

    private fun setUi() {

        mUserModel = Prefs.getObject(
            activity!!,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        tv_total_points_shop2.text = mUserModel!!.coin.toString()
        if(mUserModel!!.coin.toString().equals("0")){
            edtPointShop.isEnabled=false
            tv_note_shop2.visible()
            ShowToast("Oops, You don't have sufficient coin",requireActivity())
        }else{
            edtPointShop.filters = arrayOf<InputFilter>(InputFilterMinMax("1",  mUserModel!!.coin.toString()))

        }
        platformDialog()

        edtPlatFormShop.setSafeOnClickListener {
            dialog.show()
        }

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


        cvAdd.setSafeOnClickListener {
            shoppingMallName = edtShoppingMallName.text.toString()
            description = edtDescShop.text.toString()
            keyword = edtKeywordShop.text.toString()
            registerPoint = edtPointShop.text.toString()

            if (shoppingMallName.equals("")) {
                showToast(getString(R.string.mall_validation1), activity!!)

            } else if (description.equals("")) {

                showToast(getString(R.string.desc_validation), activity!!)


            } else if (keyword.equals("")) {

                showToast(getString(R.string.keyword_validation), activity!!)


            } else if (registerPoint.equals("")) {

                showToast(getString(R.string.point_validation), activity!!)


            } else if (platformId.equals("")) {

                showToast(getString(R.string.platform_validation), activity!!)


            } else {
                addShop()
            }
        }

    }


    private fun addShop() {

        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .addShop(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        platformId,
                        categoryId,
                        shoppingMallName,
                        description,
                        registerPoint,
                        keyword
                    ),
                TYPE = WebConstant.ADD_SHOP,
                isShowProgressDialog = true,
                apiResponseInterface = this@AddShopFragment
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
            WebConstant.GET_PLATFORM_LIST -> {
                val response = apiResponseManager.response as PlatformListModel

                when (response.status) {
                    200 -> {

                        platformAdapter.clear()
                        if (response.data!!.size > 0) {
                            platformAdapter.addAll(response.data!!)
                        } else {

                        }

                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }



            WebConstant.ADD_SHOP -> {
                val response = apiResponseManager.response as AddShopModel

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

    private fun platformDialog() {

        dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_platform)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        val imgClose = dialog.findViewById(R.id.img_close) as ImageView
        val rvPlatform = dialog.findViewById(R.id.rvPlatform) as RecyclerView

        platformAdapter = PlatformAdapter(activity!!, this)
        val linearLayoutManager1 =
            LinearLayoutManager(
                activity!!,
                LinearLayoutManager.VERTICAL,
                false
            )
        rvPlatform?.layoutManager = linearLayoutManager1
        rvPlatform?.adapter = platformAdapter
        getPlatformList()
        imgClose.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })


    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onPlatformCllick(position: Int, title: String, id: String) {
        platformId = id.toString()
        edtPlatFormShop.text = title.toString().toEditable()
        dialog.dismiss()
    }

    private fun getPlatformList() {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .getPlatformList(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_PLATFORM_LIST,
                isShowProgressDialog = false,
                apiResponseInterface = this@AddShopFragment
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