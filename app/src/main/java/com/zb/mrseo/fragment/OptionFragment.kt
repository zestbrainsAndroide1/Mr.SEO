package com.zb.mrseo.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.zb.moodlist.utility.*
import com.zb.mrseo.MainActivity
import com.zb.mrseo.R
import com.zb.mrseo.activity.ChatActivity
import com.zb.mrseo.activity.TransactionActivity
import com.zb.mrseo.adapter.ChatListAdapter
import com.zb.mrseo.adapter.HomeAdapter
import com.zb.mrseo.model.ChatListModel
import com.zb.mrseo.model.HomeModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.restapi.*
import com.zb.mrseo.utility.KeyboardUtils
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_option.*
import kotlinx.android.synthetic.main.fragment_profile.*

class OptionFragment : Fragment(),ApiResponseInterface {

    lateinit var chatListAdapter: ChatListAdapter
    var mUserModel: LoginModel.Data? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_option, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
      setUi()
    }
    private fun setUi(){
        mUserModel = Prefs.getObject(
            activity!!,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        cv_coin_option.setSafeOnClickListener {
            activity!!.start<TransactionActivity>()


        }
        cvAdmin.setSafeOnClickListener {
           rl_user.visibility=View.GONE
            rl_admin.visibility=View.VISIBLE
            cl_admin.visibility=View.VISIBLE
            cl_user.visibility=View.GONE

        }
        cvUser.setSafeOnClickListener {
            rl_user.visibility=View.VISIBLE
            rl_admin.visibility=View.GONE
            cl_admin.visibility=View.GONE
            cl_user.visibility=View.VISIBLE

        }
        chatListAdapter = ChatListAdapter(activity!!)
        val linearLayoutManager1 =
            LinearLayoutManager(
                activity!!,
                LinearLayoutManager.VERTICAL,
                false
            )
        rv_chat?.layoutManager = linearLayoutManager1
        rv_chat?.adapter = chatListAdapter

        rl_buy_points.setOnClickListener(View.OnClickListener {

            val intent = Intent( activity!!, ChatActivity::class.java)
            intent.putExtra("type","point")
            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
        rl_buy_tickets.setOnClickListener(View.OnClickListener {

            val intent = Intent( activity!!, ChatActivity::class.java)
            intent.putExtra("type","ticket")

            activity!!.startActivity(intent)
            activity!!.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })
        getChatList()


        edt_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {


                chatListAdapter?.getFilter()
                    ?.filter(edt_search.text.toString().toLowerCase())


            }

            override fun afterTextChanged(s: Editable) {


            }
        })
        edt_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    chatListAdapter?.getFilter()
                        ?.filter(edt_search.text.toString().toLowerCase())

                    return true
                }
                return false
            }
        })
        try {
            KeyboardUtils.addKeyboardToggleListener(activity!!, object :
                KeyboardUtils.SoftKeyboardToggleListener {
                override fun onToggleSoftKeyboard(isVisible: Boolean) {
                    if (isVisible) {
                        MainActivity.cvMain!!.visibility = View.GONE

                    } else {
                        MainActivity.cvMain!!.visibility = View.VISIBLE
                    }
                }
            })
        } catch (e: Exception) {

        }
    }

    private fun getChatList() {
        if (isNetworkAvailable(activity!!)) {

            ApiRequest<Any>(
                activity = activity!!,
                objectType = ApiInitialize.initialize()
                    .getChatList(
                        "Bearer ".plus(mUserModel!!.token.toString()),
                        "",
                        "1"
                    ),
                TYPE = WebConstant.GET_CHAT_LIST,
                isShowProgressDialog = true,
                apiResponseInterface = this@OptionFragment
            )

        } else {
            SnackBar.show(
                activity,
                true,
                getStr(activity!!, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.GET_CHAT_LIST -> {
                val response = apiResponseManager.response as ChatListModel

                when (response.status) {
                    200 -> {

                        chatListAdapter.clear()
                        if (response.data!!.size > 0) {
                            chatListAdapter.addAll(response.data!!)
                            rv_chat.smoothScrollToPosition(response.data!!.size)

                        } else {

                        }

                    }
                    else -> ShowToast(response.message!!, activity!!)
                }
            }


        }
    }

}