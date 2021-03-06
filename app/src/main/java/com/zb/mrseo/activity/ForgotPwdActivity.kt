package com.zb.mrseo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.start
import com.zb.mrseo.R
import kotlinx.android.synthetic.main.activity_forgot_pwd.*
import kotlinx.android.synthetic.main.activity_login.*

class ForgotPwdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pwd)
        setUi()
    }

    private fun setUi() {
        cvSend.setSafeOnClickListener {
            start<LoginActivity>()
        }
        tv_back.setSafeOnClickListener {
            start<LoginActivity>()
        }
    }

    override fun onBackPressed() {
        start<LoginActivity>()
    }
}