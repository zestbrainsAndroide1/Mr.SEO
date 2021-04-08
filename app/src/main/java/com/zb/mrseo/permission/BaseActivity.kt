package com.zb.mindme.permission

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    protected var TAG: String = ""
    protected var NOTIFICATION_TOKEN: String = ""

    protected lateinit var mContext: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        TAG = javaClass.name.substring(javaClass.name.lastIndexOf('.') + 1)
    }

    override fun onResume() {
        super.onResume()
       // Helper.setLan(this, Prefs.getValue(this@BaseActivity, AppConstant.SELECT_LNG, "en")!!)
    }

}