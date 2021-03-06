package com.zb.mrseo.activity

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.start
import com.zb.mrseo.R
import kotlinx.android.synthetic.main.activity_forgot_pwd.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    lateinit var dialog1: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setUi()
    }
    private fun setUi(){
        cvSignUp.setSafeOnClickListener {
            start<OtpActivity>()
        }
        llAccept.setSafeOnClickListener {
termsDialog()
        }
    }

    override fun onBackPressed() {
        start<LoginActivity>()
    }
    private fun termsDialog() {

        dialog1 = Dialog(this@SignUpActivity)
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialog1.setCancelable(false)
        dialog1.setContentView(R.layout.dialog_terms)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog1.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog1.window!!.attributes = lp
        val tvYes = dialog1.findViewById(R.id.cvOk) as CardView



        tvYes.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()

        })
        dialog1.show()

    }

}