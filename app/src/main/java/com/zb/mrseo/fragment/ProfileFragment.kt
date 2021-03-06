package com.zb.mrseo.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.mrseo.R
import com.zb.mrseo.SupportActivity
import com.zb.mrseo.activity.LoginActivity
import com.zb.mrseo.activity.ProductActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    lateinit var dialog1: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUi()


    }
    private fun setUi(){
        img_logout.setSafeOnClickListener {
            logoutDialog()
        }
        tv_contact_us.setOnClickListener(View.OnClickListener {

            val intent = Intent( activity!!,SupportActivity::class.java)
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
        tvTitle.text = "Are you sure you want to logout?"



        tvNo.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
        })
        tvYes.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
            val intent = Intent(activity!!, LoginActivity::class.java)
            startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            activity!!.finish()
        })
        dialog1.show()

    }

}