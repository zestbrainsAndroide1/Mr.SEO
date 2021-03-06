package com.zb.mrseo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.start
import com.zb.mrseo.R
import com.zb.mrseo.SupportActivity
import com.zb.mrseo.activity.ChatActivity
import com.zb.mrseo.activity.ForgotPwdActivity
import com.zb.mrseo.activity.LoginActivity
import com.zb.mrseo.activity.TransactionActivity
import com.zb.mrseo.adapter.ContentListAdapter
import com.zb.mrseo.adapter.HomeAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_option.*
import kotlinx.android.synthetic.main.fragment_profile.*


class ContentFragment : Fragment() {

    lateinit var contentListAdapter: ContentListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUi()
    }

    private fun setUi() {
        cvHelps.setSafeOnClickListener {
            rl_my_content.visibility=View.GONE
            rl_helps.visibility=View.VISIBLE

        }
        cvMyContens.setSafeOnClickListener {
            rl_my_content.visibility=View.VISIBLE
            rl_helps.visibility=View.GONE

        }
        contentListAdapter = ContentListAdapter(activity!!)
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
    }
}