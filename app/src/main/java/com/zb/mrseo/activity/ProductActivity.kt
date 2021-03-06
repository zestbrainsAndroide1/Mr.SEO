package com.zb.mrseo.activity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.start
import com.zb.mrseo.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.cvLogIn
import kotlinx.android.synthetic.main.activity_product.*

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT

        }
        setUi()
    }
    private fun setUi(){
        cvApplyForHelp.setSafeOnClickListener {
            start<ChatActivity>()
        }
    }
}