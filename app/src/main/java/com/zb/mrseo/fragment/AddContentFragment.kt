package com.zb.mrseo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.mrseo.R
import com.zb.mrseo.SupportActivity
import com.zb.mrseo.activity.ContentDetailsFragment
import kotlinx.android.synthetic.main.fragment_add_content.*
import kotlinx.android.synthetic.main.fragment_profile.*

class AddContentFragment : Fragment() {


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


        ll_shop.setOnClickListener(View.OnClickListener {

            val someFragment: Fragment = ContentDetailsFragment()
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