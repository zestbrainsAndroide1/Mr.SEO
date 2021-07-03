package com.zb.mrseo.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.model.CategoryModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.fragment_add_content.*
import kotlinx.android.synthetic.main.fragment_content.*

class AddContentFragment : Fragment(), ApiResponseInterface {

    var mUserModel: LoginModel.Data? = null
    public var pointList: ArrayList<String>? = null
    lateinit var img_back : ImageView
    var coin = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment.
        val view = inflater.inflate(R.layout.fragment_add_content, container, false)
        img_back = view.findViewById(R.id.img_back)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUi()
    }

    private fun setUi() {
        img_back.setOnClickListener {
            val someFragment: Fragment = HomeFragment()
            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        }



        mUserModel = Prefs.getObject(
            activity!!,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data


        pointList= ArrayList()
        getCategory()


        /** 검색 품앗이 **/
        ll_shop.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("id", "2")
            bundle.putString("point", pointList!!.get(1).toString())
            bundle.putString("coin", coin)

            val someFragment: Fragment = ContentDetailsFragment()
            someFragment!!.setArguments(bundle)

            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        })

        /** 구매 품앗이 **/
        llOnlineShop.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("id", "1")
            bundle.putString("point", pointList!!.get(0))
            bundle.putString("coin", coin)

            Log.d("point list : ", pointList.toString())

            val someFragment: Fragment = AddShopFragment()
            someFragment!!.setArguments(bundle)

            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        })

        /** 블로그 품앗이 **/
        ll_add_blog.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("id", "3")
            bundle.putString("point", pointList!!.get(2).toString())
            bundle.putString("coin", coin)

            val someFragment: Fragment = AddBlogFragment()
            someFragment!!.setArguments(bundle)

            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        })

        /** 카페 품앗이 **/
        ll_add_cafe.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("id", "4")
            bundle.putString("point", pointList!!.get(3).toString())
            bundle.putString("coin", coin)

            val someFragment: Fragment = AddCafeFragment()
            someFragment!!.setArguments(bundle)

            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        })
    }

    fun onBackPressed() {
        activity!!.supportFragmentManager.popBackStack()
    }

    private fun getCategory() {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .getCategory(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_CATEGORY,
                isShowProgressDialog = true,
                apiResponseInterface = this@AddContentFragment
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

            WebConstant.GET_CATEGORY -> {
                val response = apiResponseManager.response as CategoryModel
                tv_coin_count.text = response.coin
                coin = response.coin!!

                when (response.status) {
                    200 -> {
                        for (i in 0 until response.data!!.size) {
                            pointList!!.add(response.data!!.get(i).categoryCoin.toString())
                        }
                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }


        }
    }

}