package com.zb.mrseo.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.mrseo.R
import com.zb.mrseo.adapter.HomeAdapter
import com.zb.mrseo.adapter.TransactionAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_transaction_activity.*


class TransactionActivity: AppCompatActivity() {
    lateinit var transactionAdapter:TransactionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_transaction_activity)

        transactionAdapter = TransactionAdapter(this@TransactionActivity)
        val linearLayoutManager1 =
            LinearLayoutManager(
               this@TransactionActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_transaction?.layoutManager = linearLayoutManager1
        rv_transaction?.adapter = transactionAdapter

    }

}