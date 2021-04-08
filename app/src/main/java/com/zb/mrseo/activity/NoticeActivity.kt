package com.zb.mrseo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.adapter.NoticeAdapter
import com.zb.mrseo.adapter.TransactionAdapter
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.NoticeModel
import com.zb.mrseo.model.TransactionModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_edit_shop.*
import kotlinx.android.synthetic.main.activity_notice.*
import kotlinx.android.synthetic.main.fragment_transaction_activity.*

class NoticeActivity : AppCompatActivity() , ApiResponseInterface {
    lateinit var noticeAdapter: NoticeAdapter
    var mUserModel: LoginModel.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)
        setUi()
    }
    private fun setUi() {
        mUserModel = Prefs.getObject(
            this@NoticeActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        img_back_notice.setSafeOnClickListener {
            onBackPressed()
        }

        noticeAdapter = NoticeAdapter(this@NoticeActivity)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@NoticeActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_notice?.layoutManager = linearLayoutManager1
        rv_notice?.adapter = noticeAdapter
        getNoticeData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun getNoticeData() {
        if (isNetworkAvailable(this@NoticeActivity)) {

            ApiRequest<Any>(
                activity = this@NoticeActivity,
                objectType = ApiInitialize.initialize()
                    .getNotice(
                        "Bearer ".plus(mUserModel!!.token.toString())
                    ),
                TYPE = WebConstant.GET_NOTICE,
                isShowProgressDialog = true,
                apiResponseInterface = this@NoticeActivity
            )

        } else {
            SnackBar.show(
                this@NoticeActivity,
                true,
                getStr(this@NoticeActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_NOTICE -> {
                val response = apiResponseManager.response as NoticeModel

                when (response.status) {
                    200 -> {


                       noticeAdapter.clear()
                        if (response.data!!.size > 0) {
                            noticeAdapter.addAll(response.data!!)
                        } else {

                        }

                    }
                    else -> ShowToast(response.message!!, this@NoticeActivity)
                }
            }


        }
    }

}