package com.zb.mrseo.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.adapter.AdminMsgAdapter
import com.zb.mrseo.model.*
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.AppConstants
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class ChatActivity : AppCompatActivity(), ApiResponseInterface {
    lateinit var messageAdapter: AdminMsgAdapter
    var threadId: String = "1"
    var mUserModel: LoginModel.Data? = null
    var title: String = ""
    var type: String = ""

    private val broadCastChat = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val data = intent.getSerializableExtra("data") as HashMap<String, String>
            getChatHistory(false, data["object_id"].toString())

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        setUi()
    }

    fun setUi() {
        val extras = intent.extras
        if (extras != null) {
            threadId = extras.getString("id", "")
            title = extras.getString("title", "")
            type = extras.getString("type", "")


        }
        img_send.setSafeOnClickListener {
            if (!edt_msg.text.toString().equals("")) {
                sendMessage()
                edt_msg.text.clear()
            } else {
                showToast(getString(R.string.msg_validation), this@ChatActivity)
            }
        }
        img_back_chat.setSafeOnClickListener {
            onBackPressed()
        }

        mUserModel = Prefs.getObject(
            this@ChatActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        messageAdapter = AdminMsgAdapter(this@ChatActivity)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@ChatActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_msg?.layoutManager = linearLayoutManager1
        rv_msg?.adapter = messageAdapter

        if(threadId.equals("0")){

        }else{
            AppConstants.OPEND_THREAD = threadId

            getChatHistory(true,threadId)

        }
/*
        getChatHistory(true, "")
*/
    }

    private fun getChatHistory(show: Boolean, threadId1: String) {
        if (isNetworkAvailable(this@ChatActivity)) {

            ApiRequest<Any>(
                activity = this@ChatActivity,
                objectType = ApiInitialize.initialize()
                    .getAdminChat(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        "",
                        "",
                        type
                    ),
                TYPE = WebConstant.GET_ADMIN_CHAT,
                isShowProgressDialog = show,
                apiResponseInterface = this@ChatActivity
            )

        } else {
            SnackBar.show(
                this@ChatActivity,
                true,
                getStr(this@ChatActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_ADMIN_CHAT -> {
                val response = apiResponseManager.response as AdminChatModel

                when (response.status) {
                    200 -> {
                        AppConstants.OPEND_THREAD = response.data!!.get(0).threadsId.toString()

                        messageAdapter.clear()
                        if (response.data!!.size > 0) {
                            messageAdapter.addAll(response.data!!)
                            rv_msg.smoothScrollToPosition(response.data!!.size)

                        } else {

                        }

                    }
                    else -> ShowToast(response.message!!, this@ChatActivity)
                }
            }

            WebConstant.SEND_ADMIN_MSG -> {
                val response = apiResponseManager.response as AdminMsgModel
                when (response.status) {
                    200 -> {
                        edt_msg.text!!.clear()
                        hideKeyboard()
                        var id1:String=response.data!!.threadsId.toString()
                        AppConstants.OPEND_THREAD =id1

                        getChatHistory(false,id1)

                    }


                    else -> ShowToast(response.message!!, this@ChatActivity)
                }


            }

        }
    }

    override fun onResume() {
        super.onResume()
        AppConstants.ISCHAT_ACTVITY_OPEN = true
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastChat, IntentFilter("chat"))
    }

    override fun onPause() {
        super.onPause()
        AppConstants.ISCHAT_ACTVITY_OPEN = false
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadCastChat)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppConstants.ISCHAT_ACTVITY_OPEN = false
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadCastChat)
    }

    private fun sendMessage() {
        if (isNetworkAvailable(this@ChatActivity)) {

            ApiRequest<Any>(
                activity = this@ChatActivity,
                objectType = ApiInitialize.initialize()
                    .sendAdminMsg(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        edt_msg.text.toString(),
                        "message",
                        type
                    ),
                TYPE = WebConstant.SEND_ADMIN_MSG,
                isShowProgressDialog = false,
                apiResponseInterface = this@ChatActivity
            )

        } else {
            SnackBar.show(
                this@ChatActivity,
                true,
                getStr(this@ChatActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }
    override fun onBackPressed() {

            val intent = Intent(this@ChatActivity, MainActivity::class.java)
            intent.putExtra("show","option_admin")
            startActivity(intent)
            overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            finish()


    }
}