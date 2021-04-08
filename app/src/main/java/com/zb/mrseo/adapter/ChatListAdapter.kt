package com.zb.mrseo.adapter



import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zb.mrseo.R
import com.zb.mrseo.activity.ChatActivity
import com.zb.mrseo.activity.ChatHistoryActivity
import com.zb.mrseo.activity.ProductActivity
import com.zb.mrseo.model.ChatListModel
import com.zb.mrseo.model.HomeModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatListAdapter(private val mActivity: Context) :
    RecyclerView.Adapter<ChatListAdapter.MyViewHolder>(), Filterable {

    private var mModel = ArrayList<ChatListModel.Datum>()
    private var filteredData = mModel

    init {
        filteredData = ArrayList<ChatListModel.Datum>()
        this.filteredData = mModel as ArrayList<ChatListModel.Datum>
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var llMain: ConstraintLayout = itemView.findViewById(R.id.ll_chat)
        var tvUser: TextView = itemView.findViewById(R.id.tv_user_name)
        var tvMsg: TextView = itemView.findViewById(R.id.tv_msg)
        var tvTime: TextView = itemView.findViewById(R.id.tv_time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_chat, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {

        holder.tvMsg.text=filteredData[listPosition].messageLatest!!.message.toString()
        holder.tvUser.text=filteredData[listPosition].senderUser!!.name.toString()
        holder.tvTime.text=getUTCTOLOCALFromServer(filteredData[listPosition].messageLatest!!.createdAt.toString())

        holder.llMain.setOnClickListener(View.OnClickListener {

            val intent = Intent(mActivity, ChatHistoryActivity::class.java)
            intent.putExtra("id",filteredData[listPosition].messageLatest!!.threadsId.toString())
            intent.putExtra("title",filteredData[listPosition].senderUser!!.name.toString())
            intent.putExtra("receiverId",filteredData[listPosition].messageLatest!!.senderId.toString())

            (mActivity as Activity).startActivity(intent)
            (mActivity as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })


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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    filteredData = mModel as ArrayList<ChatListModel.Datum>
                } else {
                    val filteredList = mutableListOf<ChatListModel.Datum>()
                    for (row in mModel) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.senderUser!!.name!!.toLowerCase(Locale.ROOT).contains(charSequence)) {
                            filteredList.add(row)
                        }
                    }

                    filteredData = filteredList as ArrayList<ChatListModel.Datum>
                }

                val filterResults = FilterResults()
                filterResults.values = filteredData
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredData = filterResults.values as ArrayList<ChatListModel.Datum>

                // refresh the list with filtered data
                notifyDataSetChanged()
               /* if(filteredData.size==0){
                    llData.visibility=View.VISIBLE
                }else{
                    llData.visibility=View.GONE

                }*/
            }
        }
    }


    override fun getItemCount(): Int {
        return filteredData.size
    }

    fun addAll(mData: ArrayList<ChatListModel.Datum>?) {
        filteredData.addAll(mData!!)
        notifyItemInserted(filteredData.size - 1)
        notifyDataSetChanged()
    }

    fun clear() {
        filteredData.clear()
        notifyDataSetChanged()
    }
}