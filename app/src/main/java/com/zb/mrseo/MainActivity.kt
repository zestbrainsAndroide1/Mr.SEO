package com.zb.mrseo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.start
import com.zb.mrseo.activity.ForgotPwdActivity
import com.zb.mrseo.fragment.*
import com.zb.mrseo.model.DataModel
import com.zb.mrseo.utility.BaseFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var dialog1: Dialog
    var show: String = ""
    var img: String = ""

    companion object {
        lateinit var cvMain: CardView

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val extras = intent.extras
        if (extras != null) {
            img = extras.getString("img", "")


        }
        if(!img.equals("")){
            welcomeDialog()
        }
        setUi()

    }

    private fun setUi() {
        val extras = intent.extras
        if (extras != null) {
            show = extras.getString("show", "")


        }
        cvMain = findViewById(R.id.llBottom)


        if (show.equals("content")) {
            replaceFragment(ContentFragment())

            setBottomIcon()
            imgContent.setImageResource(R.drawable.content_select)
        } else if (show.equals("content1")) {
            val bundle = Bundle()
            bundle.putString("show", "help")
            val contentFragment: Fragment = ContentFragment()
            contentFragment!!.setArguments(bundle)

            replaceFragment(contentFragment)

            setBottomIcon()
            imgContent.setImageResource(R.drawable.content_select)
        }else if (show.equals("option")) {
            val bundle = Bundle()
            bundle.putString("show", "user")
            val contentFragment: Fragment = OptionFragment()
            contentFragment!!.setArguments(bundle)

            replaceFragment(contentFragment)

            setBottomIcon()
            imgContent.setImageResource(R.drawable.chat_select)
        } else if (show.equals("option_admin")) {
            val bundle = Bundle()
            bundle.putString("show", "admin")
            val contentFragment: Fragment = OptionFragment()
            contentFragment!!.setArguments(bundle)

            replaceFragment(contentFragment)

            setBottomIcon()
            imgContent.setImageResource(R.drawable.chat_select)
        } else if (show.equals("profile")) {
            replaceFragment(UserDetailFragment())

            setBottomIcon()
            imgProfile.setImageResource(R.drawable.profile_select)
        } else {
            setBottomIcon()
            imgHome.setImageResource(R.drawable.home_select)
            replaceFragment(HomeFragment())
        }






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
            replaceFragment(UserDetailFragment())

            setBottomIcon()
            imgProfile.setImageResource(R.drawable.profile_select)
        }

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

    private fun welcomeDialog() {

        dialog1 = Dialog(this@MainActivity)
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialog1.setCancelable(false)
        dialog1.setContentView(R.layout.dialog_welcome)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog1.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog1.window!!.attributes = lp
        val imgClose = dialog1.findViewById(R.id.img_close) as ImageView
        val imgWelcome = dialog1.findViewById(R.id.img_welcome) as PorterShapeImageView

        Glide.with(this@MainActivity).load(img).into(imgWelcome)
        imgClose.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
        })

        dialog1.show()

    }


}