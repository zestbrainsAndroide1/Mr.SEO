package com.zb.mrseo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.start
import com.zb.mrseo.activity.ForgotPwdActivity
import com.zb.mrseo.fragment.ContentFragment
import com.zb.mrseo.fragment.HomeFragment
import com.zb.mrseo.fragment.OptionFragment
import com.zb.mrseo.fragment.ProfileFragment
import com.zb.mrseo.utility.BaseFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var dialog1: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBottomIcon()
        imgHome.setImageResource(R.drawable.home_select)
        replaceFragment(HomeFragment())

        llHome.setSafeOnClickListener {

            replaceFragment(HomeFragment())

            setBottomIcon()
            imgHome.setImageResource(R.drawable.home_select)
        }

        llContent.setSafeOnClickListener {
            replaceFragment(ContentFragment())

            setBottomIcon()
            imgContent.setImageResource(R.drawable.content_select)
        }

        llChat.setSafeOnClickListener {
            replaceFragment(OptionFragment())

            setBottomIcon()
            imgChat.setImageResource(R.drawable.chat_select)
        }

        llProfile.setSafeOnClickListener {
            replaceFragment(ProfileFragment())

            setBottomIcon()
            imgProfile.setImageResource(R.drawable.profile_select)         }

    }

    private fun setBottomIcon() {
        imgHome.setImageResource(R.drawable.ic_home)
        imgContent.setImageResource(R.drawable.ic_content)
        imgChat.setImageResource(R.drawable.chat)
        imgProfile.setImageResource(R.drawable.ic_profile)

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frmContainer, fragment)
            .commit()
    }
    private fun tellFragments() {
        val fragments = supportFragmentManager.fragments
        for (f in fragments) {
            if (f != null && f is BaseFragment) f.onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
exitDialog()
        } else {


            tellFragments()


            super.onBackPressed()
        }


    }
    private fun exitDialog() {

        dialog1 = Dialog(this@MainActivity)
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


        tvNo.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
        })
        tvYes.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()

            finish()
        })
        dialog1.show()

    }


}