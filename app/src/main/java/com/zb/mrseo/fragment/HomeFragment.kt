package com.zb.mrseo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.start
import com.zb.mrseo.R
import com.zb.mrseo.activity.ForgotPwdActivity
import com.zb.mrseo.activity.TransactionActivity
import com.zb.mrseo.adapter.HomeAdapter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {
    lateinit var homeAdapter: HomeAdapter

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
    }

}