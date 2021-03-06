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
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zb.mrseo.R
import com.zb.mrseo.activity.ChatActivity
import com.zb.mrseo.activity.ProductActivity


class MessageAdapter(private val mActivity: Context
) :
    RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


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



    }

    override fun getItemCount(): Int {
        return 4
    }

    /* fun addAll(mData: ArrayList<MemberModel.Datum>?) {
         filteredData.addAll(mData!!)
         notifyItemInserted(filteredData.size - 1)
         notifyDataSetChanged()
     }

     fun clear() {
         filteredData.clear()
         notifyDataSetChanged()
     }*/
}