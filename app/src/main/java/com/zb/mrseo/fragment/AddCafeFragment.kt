package com.zb.mrseo.fragment

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
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
import com.zb.mrseo.model.AddBlogModel
import com.zb.mrseo.model.AddCafeModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.PlatformListModel
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.InputFilterMinMax
import com.zb.mrseo.utility.KeyboardUtils
import kotlinx.android.synthetic.main.fragment_add_blog.*
import kotlinx.android.synthetic.main.fragment_add_cafe.*
import kotlinx.android.synthetic.main.fragment_content_details.*


class AddCafeFragment : Fragment(), ApiResponseInterface, OnPlatformClick {

    var platformId: String = ""
    var categoryId: String = ""
    var cafeName: String = ""
    var url: String = ""
    var description: String = ""
    var registerPoint: String = ""
    var mUserModel: LoginModel.Data? = null
    lateinit var dialog: Dialog
    lateinit var platformAdapter: PlatformAdapter
    var point=""
    var coin =""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_cafe, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            categoryId = bundle.getString("id").toString()
            point = bundle.getString("point").toString()
            coin  = bundle.getString("coin").toString()

        }

        setUi()
    }

    private fun setUi() {

        mUserModel = Prefs.getObject(
            activity!!,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        edtHelpersCafe.addTextChangedListener(object : TextWatcher {
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
                    var helper=edtHelpersCafe.text.toString()
                    var total=helper.toInt()*point.toInt()
                    edtPointCafe.setText(total.toString())
                    if(total > coin.toInt()){
                        edtPointCafe.isEnabled=false
                        tv_note_cafe.visible()
                        ShowToast("Oops, You don't have sufficient coin",requireActivity())
                    }else{
                        tv_note_cafe.gone()

                    }
                }catch(e:Exception){

                }



            }

            override fun afterTextChanged(s: Editable) {


            }
        })

        platformDialog()

        /*edtTitleCafe.setSafeOnClickListener {
            dialog.show()
        }
*/
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


        cvAddCafe.setSafeOnClickListener {
            cafeName = edtCafeName.text.toString()
            description = edtDescCafe.text.toString()
            url = edtCafeUrl.text.toString()
            registerPoint = edtPointCafe.text.toString()

            if (cafeName.equals("")) {
                showToast(getString(R.string.cafe_validation1), activity!!)

            } else if (description.equals("")) {

                showToast(getString(R.string.desc_validation), activity!!)


            } else if (url.equals("")) {

                showToast(getString(R.string.url_validation), activity!!)


            } else if (registerPoint.equals("")) {

                showToast(getString(R.string.point_validation), activity!!)


            } else {
                addCafe()
            }
        }

    }


    private fun addCafe() {

        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .addCafe(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        edtTitleCafe.text.toString(),
                        categoryId,
                        cafeName,
                        description,
                        (registerPoint.toInt() / point.toInt()).toString(),
                        url
                    ),
                TYPE = WebConstant.ADD_CAFE,
                isShowProgressDialog = true,
                apiResponseInterface = this@AddCafeFragment
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


            WebConstant.ADD_CAFE -> {
                val response = apiResponseManager.response as AddCafeModel

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
        edtTitleCafe.text = title.toString().toEditable()
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
                apiResponseInterface = this@AddCafeFragment
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