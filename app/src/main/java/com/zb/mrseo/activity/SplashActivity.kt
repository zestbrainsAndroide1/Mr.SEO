package com.zb.mrseo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import com.zb.moodlist.utility.AppConstant
import com.zb.moodlist.utility.Prefs
import com.zb.moodlist.utility.start
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({



            if (Prefs.contain(this@SplashActivity, AppConstant.ACCOUNT_DATA)) {
                start<MainActivity>()
                finishAffinity()


            }else{
                start<LoginActivity>()
                finishAffinity()
            }






        }, 3000)
    }
}