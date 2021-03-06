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
import com.zb.mrseo.activity.ChatActivity
import com.zb.mrseo.activity.TransactionActivity
import com.zb.mrseo.adapter.ChatListAdapter
import com.zb.mrseo.adapter.HomeAdapter
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_option.*
import kotlinx.android.synthetic.main.fragment_profile.*

class OptionFragment : Fragment() {

    lateinit var chatListAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
      setUi()
    }
    private fun setUi(){
        cv_coin_option.setSafeOnClickListener {
            activity!!.start<TransactionActivity>()


        }
        cvAdmin.setSafeOnClickListener {
           rl_user.visibility=View.GONE
            rl_admin.visibility=View.VISIBLE
            cl_admin.visibility=View.VISIBLE
            cl_user.visibility=View.GONE

        }
        cvUser.setSafeOnClickListener {
            rl_user.visibility=View.VISIBLE
            rl_admin.visibility=View.GONE
            cl_admin.visibility=View.GONE
            cl_user.visibility=View.VISIBLE

        }
        chatListAdapter = ChatListAdapter(activity!!)
        val linearLayoutManager1 =
            LinearLayoutManager(
                activity!!,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_chat?.layoutManager = linearLayoutManager1
        rv_chat?.adapter = chatListAdapter

        rl_buy_points.setOnClickListener(View.OnClickListener {

            val intent = Intent( activity!!, ChatActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
        rl_buy_tickets.setOnClickListener(View.OnClickListener {

            val intent = Intent( activity!!, ChatActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
    }
}