package com.zb.mrseo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.start
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_otp.*

class OtpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        setUi()

    }

    private fun setUi() {
        cvVerify.setSafeOnClickListener {
            start<MainActivity>()
        }
    }

    override fun onBackPressed() {
        start<SignUpActivity>()
    }
}