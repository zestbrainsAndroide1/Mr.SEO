package com.zb.mrseo.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.adapter.HomeAdapter
import com.zb.mrseo.adapter.TransactionAdapter
import com.zb.mrseo.model.HomeModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.TransactionModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_transaction_activity.*


class TransactionActivity : AppCompatActivity(), ApiResponseInterface {
    lateinit var transactionAdapter: TransactionAdapter
    var mUserModel: LoginModel.Data? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_transaction_activity)
        setUi()

    }

    private fun setUi() {
        mUserModel = Prefs.getObject(
            this@TransactionActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        tv_user_name_transaction.text=mUserModel!!.name.toString()
        transactionAdapter = TransactionAdapter(this@TransactionActivity)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@TransactionActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_transaction?.layoutManager = linearLayoutManager1
        rv_transaction?.adapter = transactionAdapter
        getTransactionData()
    }

    private fun getTransactionData() {
        if (isNetworkAvailable(this@TransactionActivity)) {

            ApiRequest<Any>(
                activity = this@TransactionActivity,
                objectType = ApiInitialize.initialize()
                    .getTransactionList(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_TRANSACTION_LIST,
                isShowProgressDialog = true,
                apiResponseInterface = this@TransactionActivity
            )

        } else {
            SnackBar.show(
                this@TransactionActivity,
                true,
                getStr(this@TransactionActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_TRANSACTION_LIST -> {
                val response = apiResponseManager.response as TransactionModel

                when (response.status) {
                    200 -> {

                        transactionAdapter.clear()
                        if (response.data!!.size > 0) {
                            transactionAdapter.addAll(response.data!!)
                        } else {

                        }

                    }
                    else -> ShowToast(response.message!!, this@TransactionActivity)
                }
            }


        }
    }

}