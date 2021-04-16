package com.zb.mrseo.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.zb.moodlist.utility.AppConstant
import com.zb.moodlist.utility.Prefs
import com.zb.mrseo.R
import com.zb.mrseo.model.LoginModel
import kotlinx.android.synthetic.main.fragment_add_content.*
import kotlinx.android.synthetic.main.fragment_content.*

class AddContentFragment : Fragment() {

    var mUserModel: LoginModel.Data? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_content, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUi()
    }
    private fun setUi(){
        mUserModel = Prefs.getObject(
            activity!!,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        tv_coin_count.text=mUserModel!!.coin.toString()


        ll_shop.setOnClickListener(View.OnClickListener {

            val bundle = Bundle()
            bundle.putString("id", "2")

            val someFragment: Fragment = ContentDetailsFragment()
            someFragment!!.setArguments(bundle)

            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        })
        llOnlineShop.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("id", "1")

            val someFragment: Fragment = AddShopFragment()
            someFragment!!.setArguments(bundle)

            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        })

        ll_add_blog.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("id", "3")

            val someFragment: Fragment =AddBlogFragment()
            someFragment!!.setArguments(bundle)

            val transaction: FragmentTransaction = fragmentManager!!.beginTransaction()
            transaction.replace(R.id.frmContainer, someFragment)
            transaction.addToBackStack(null) // if written, this transaction will be added to backstack
            transaction.commit()
        })

        ll_add_cafe.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("id", "4")

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
}