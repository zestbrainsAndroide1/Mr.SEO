package com.zb.mrseo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.start
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUi()
    }

    private fun setUi() {
        tv_forgot_pwd.setSafeOnClickListener {
            start<ForgotPwdActivity>()
        }
        ll_sign_up.setSafeOnClickListener {
            start<SignUpActivity>()
        }
       cvLogIn.setSafeOnClickListener {
            start<MainActivity>()
        }
    }
}