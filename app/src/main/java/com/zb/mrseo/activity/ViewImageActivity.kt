package com.zb.mrseo.activity

import android.content.Intent
import com.zb.mrseo.R


import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler
import com.bumptech.glide.Glide
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.mrseo.MainActivity
import kotlinx.android.synthetic.main.activity_notice.*

import kotlinx.android.synthetic.main.activity_view_image.*
import java.security.AccessController.getContext
import java.util.ArrayList

class ViewImageActivity : AppCompatActivity() {
    var url: String = ""
    var postId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)
        val extras = intent.extras
        if (extras != null) {

            url = extras.getString("url", "")
           postId = extras.getString("id", "")


        }
        Glide.with(this@ViewImageActivity).load(url).into(imgPost)
        img_back_view.setSafeOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this@ViewImageActivity, HelpActivity::class.java)
        intent.putExtra("id",postId)

        startActivity(intent)
        overridePendingTransition(
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        finish()    }
}