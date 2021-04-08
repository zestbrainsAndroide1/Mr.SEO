package com.zb.mrseo.adapter




import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zb.moodlist.utility.AppConstant
import com.zb.moodlist.utility.Prefs
import com.zb.moodlist.utility.gone
import com.zb.mrseo.R
import com.zb.mrseo.activity.ChatActivity
import com.zb.mrseo.activity.ProductActivity
import com.zb.mrseo.model.AdminChatModel
import com.zb.mrseo.model.ChatListModel
import com.zb.mrseo.model.ChatModel
import com.zb.mrseo.model.LoginModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AdminMsgAdapter(private val mActivity: Context
) :
    RecyclerView.Adapter<AdminMsgAdapter.MyViewHolder>() {

    private var mModel = ArrayList<AdminChatModel.Datum>()
    var mUserModel: LoginModel.Data? = null

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var llReceiver: LinearLayout = itemView.findViewById(R.id.ll_receiver)
        var llSender: LinearLayout = itemView.findViewById(R.id.ll_sender)
        var tvSenderMsg: TextView = itemView.findViewById(R.id.tv_sender_msg)
        var tvSenderMsgTime: TextView = itemView.findViewById(R.id.tv_sender_msg_time)
        var tvReceiverMsg: TextView = itemView.findViewById(R.id.tv_receiver_msg)
        var tvReceiverMsgTime: TextView = itemView.findViewById(R.id.tv_receiver_time)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_msg, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {
        mUserModel = Prefs.getObject(
            mActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data

        if(mModel[listPosition].senderId.toString().equals(mUserModel!!.id.toString())){
            holder.llReceiver.gone()
            holder.tvSenderMsg.text=mModel[listPosition].message.toString()
            holder.tvSenderMsgTime.text=getUTCTOLOCALFromServer(mModel[listPosition].createdAt.toString())

        }else{
            holder.llSender.gone()
            holder.tvReceiverMsg.text=mModel[listPosition].message.toString()
            holder.tvReceiverMsgTime.text=getUTCTOLOCALFromServer(mModel[listPosition].createdAt.toString())

        }



    }


    open fun getUTCTOLOCALFromServer(originalString: String?): String? {
        var newstr = ""
        try {
            val sDF = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)
            sDF.timeZone = TimeZone.getTimeZone("UTC")
            val date = sDF.parse(originalString)
            newstr = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(date)
            println(
                """
                
                $newstr
                
                """.trimIndent()
            )
        } catch (e: ParseException) {
            //Handle exception here
            e.printStackTrace()
        }
        return newstr
    }

    override fun getItemCount(): Int {
        return mModel.size
    }

    fun addAll(mData: ArrayList<AdminChatModel.Datum>?) {
        mModel.addAll(mData!!)
        notifyItemInserted(mModel.size - 1)
        notifyDataSetChanged()
    }

    fun clear() {
        mModel.clear()
        notifyDataSetChanged()
    }
}