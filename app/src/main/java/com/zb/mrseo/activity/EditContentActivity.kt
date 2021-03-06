package com.zb.mrseo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.start
import com.zb.mrseo.R
import kotlinx.android.synthetic.main.activity_edit_content2.*
import kotlinx.android.synthetic.main.activity_shop_details.*

class EditContentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_content2)
        setUi()
    }
    private fun setUi(){
        img_back.setSafeOnClickListener {
            onBackPressed()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}