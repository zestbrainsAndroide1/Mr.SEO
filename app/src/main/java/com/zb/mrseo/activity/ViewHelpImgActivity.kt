package com.zb.mrseo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.mrseo.R
import kotlinx.android.synthetic.main.activity_view_help.*
import kotlinx.android.synthetic.main.activity_view_image.*
import kotlinx.android.synthetic.main.activity_view_image.img_back_view

class ViewHelpImgActivity : AppCompatActivity() {

    var type: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_help)

        val extras = intent.extras
        if (extras != null) {

            type = extras.getString("type", "")


        }
        if (type.equals("1")) {
            imgViewHelp.setImageResource(R.drawable.img_detail1)
        } else {
            imgViewHelp.setImageResource(R.drawable.img_detail2)

        }
        img_back_view.setSafeOnClickListener {
            onBackPressed()
        }


    }

    override fun onBackPressed() {
        finish()
    }
}