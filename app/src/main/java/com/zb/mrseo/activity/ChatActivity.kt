package com.zb.mrseo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.mrseo.R
import com.zb.mrseo.adapter.HomeAdapter
import com.zb.mrseo.adapter.MessageAdapter
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_home.*

class ChatActivity : AppCompatActivity() {
    lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

       messageAdapter = MessageAdapter(this@ChatActivity)
        val linearLayoutManager1 =
            LinearLayoutManager(
               this@ChatActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_msg?.layoutManager = linearLayoutManager1
        rv_msg?.adapter = messageAdapter
    }
}