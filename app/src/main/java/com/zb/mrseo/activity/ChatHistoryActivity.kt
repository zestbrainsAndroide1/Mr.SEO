package com.zb.mrseo.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.adapter.MessageAdapter
import com.zb.mrseo.model.ChatListModel
import com.zb.mrseo.model.ChatModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.model.SendMsgModel
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.AppConstants
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_login.*
import java.util.HashMap

class ChatHistoryActivity : AppCompatActivity(),ApiResponseInterface {
    lateinit var messageAdapter: MessageAdapter
    var threadId:String=""
    var mUserModel: LoginModel.Data? = null
    var title:String=""
    var receiverId: String = ""
    var type:String=""


    private val broadCastChat = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val data = intent.getSerializableExtra("data") as HashMap<String, String>
            getChatHistory(false,data["object_id"].toString())

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setUi()
    }

    fun setUi(){
        val extras = intent.extras
        if (extras != null) {
            threadId= extras.getString("id", "")
            title= extras.getString("title", "")
            receiverId = extras.getString("receiverId").toString()
            type= extras.getString("type", "")

            Log.d("threadId : ", threadId.toString())
            Log.d("title : ", title.toString())
            Log.d("receiverId : ", receiverId.toString())
            Log.d("type : ", type.toString())

        }

        img_back_chat.setSafeOnClickListener {
           onBackPressed()
        }

        img_send.setSafeOnClickListener {
            if(!edt_msg.text.toString().equals("")){
                sendMessage()
                edt_msg.text.clear()

            }else{
                showToast(getString(R.string.msg_validation),this@ChatHistoryActivity)
            }
        }



        tv_user_name_chat.text=title.toString()
        mUserModel = Prefs.getObject(
            this@ChatHistoryActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        messageAdapter = MessageAdapter(this@ChatHistoryActivity)
        val linearLayoutManager1 =
            LinearLayoutManager(
                this@ChatHistoryActivity,
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

    }

    private fun sendMessage() {
        if (isNetworkAvailable(this@ChatHistoryActivity)) {

            ApiRequest<Any>(
                activity = this@ChatHistoryActivity,
                objectType = ApiInitialize.initialize()
                    .sendMsg(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        threadId,
                        receiverId,
                        edt_msg.text.toString(),
                        "message"
                    ),
                TYPE = WebConstant.SEND_MSG,
                isShowProgressDialog = false,
                apiResponseInterface = this@ChatHistoryActivity
            )

        } else {
            SnackBar.show(
                this@ChatHistoryActivity,
                true,
                getStr(this@ChatHistoryActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    private fun getChatHistory(show: Boolean,threadId1:String) {
        if (isNetworkAvailable(this@ChatHistoryActivity)) {

            ApiRequest<Any>(
                activity = this@ChatHistoryActivity,
                objectType = ApiInitialize.initialize()
                    .getChat(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        "",
                        "0",
                        threadId1
                    ),
                TYPE = WebConstant.GET_CHAT,
                isShowProgressDialog = show,
                apiResponseInterface = this@ChatHistoryActivity
            )

        } else {
            SnackBar.show(
                this@ChatHistoryActivity,
                true,
                getStr(this@ChatHistoryActivity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_CHAT -> {
                val response = apiResponseManager.response as ChatModel

                when (response.status) {
                    200 -> {

                        if(type.equals("helper_post") || type.equals("notification")){
                            receiverId=response.data?.get(0)?.receiverId.toString()
                            title=response.data?.get(0)?.receiverName.toString()
                            tv_user_name_chat.text=title.toString()

                        }

                        messageAdapter.clear()
                        if (response.data!!.size > 0) {
                            messageAdapter.addAll(response.data!!)
                            rv_msg.smoothScrollToPosition(response.data!!.size)

                        } else {

                        }

                    }
                    else -> ShowToast(response.message!!, this@ChatHistoryActivity)
                }
            }

            WebConstant.SEND_MSG -> {
                val response = apiResponseManager.response as SendMsgModel
                when (response.status) {
                    200 -> {
                        edt_msg.text!!.clear()
                        hideKeyboard()
                        var id1:String=response.data!!.threadsId.toString()
                        AppConstants.OPEND_THREAD =id1

                        getChatHistory(false,id1)

                    }


                    else -> ShowToast(response.message!!, this@ChatHistoryActivity)
                }


            }

        }
    }

    override fun onResume() {
        super.onResume()
        AppConstants.ISCHAT_ACTVITY_OPEN = true
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this).registerReceiver(broadCastChat, IntentFilter("chat"))
    }

    override fun onPause() {
        super.onPause()
        AppConstants.ISCHAT_ACTVITY_OPEN = false
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastChat)
    }

    override fun onDestroy() {
        super.onDestroy()
        AppConstants.ISCHAT_ACTVITY_OPEN = false
        androidx.localbroadcastmanager.content.LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastChat)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("chat", "채팅")
        startActivity(intent)
        finish()
//        if(type.equals("details")){
//            val intent = Intent(this@ChatHistoryActivity, PostDetailActivity::class.java)
//            intent.putExtra("show","")
//            startActivity(intent)
//            overridePendingTransition(
//                R.anim.slide_in_right,
//                R.anim.slide_out_left
//            )
//            finish()
//        }else if(type.equals("help")){
//            onBackPressed()
//        }else if(type.equals("help_list")){
//            onBackPressed()
//
//        }else if(type.equals("helper_post")){
//            onBackPressed()
//        }else{
//            val intent = Intent(this@ChatHistoryActivity, MainActivity::class.java)
//            intent.putExtra("show","option")
//            startActivity(intent)
//            overridePendingTransition(
//                R.anim.slide_in_right,
//                R.anim.slide_out_left
//            )
//            finish()
//        }

    }
}