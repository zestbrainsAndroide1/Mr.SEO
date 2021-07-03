package com.zb.mrseo.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zb.mrseo.R
import com.zb.mrseo.activity.ChatActivity
import com.zb.mrseo.activity.ProductActivity
import com.zb.mrseo.model.HomeModel
import com.zb.mrseo.model.TransactionModel


class TransactionAdapter(
    private val mActivity: Context
) :
    RecyclerView.Adapter<TransactionAdapter.MyViewHolder>() {

    private var mModel = ArrayList<TransactionModel.Datum>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvCoins: TextView = itemView.findViewById(R.id.tv_total_coins)
        var tvDate: TextView = itemView.findViewById(R.id.tv_date)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_status)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_transaction, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {
        holder.tvCoins.text = mModel[listPosition].amount.toString() + " 포인트"
        holder.tvDate.text = mModel[listPosition].createdAt.toString()

        when(mModel[listPosition].type.toString()){
            "Received" -> holder.tvStatus.text = "받음"
            "Used" -> holder.tvStatus.text = "사용"
        }

        if (mModel[listPosition].type.toString().equals("Used")) {
            holder.tvStatus.setTextColor(Color.parseColor("#FF0000"))

        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#69B95C"))


        }


    }


    override fun getItemCount(): Int {
        return mModel.size
    }

    fun addAll(mData: ArrayList<TransactionModel.Datum>?) {
        mModel.addAll(mData!!)
        notifyItemInserted(mModel.size - 1)
        notifyDataSetChanged()
    }

    fun clear() {
        mModel.clear()
        notifyDataSetChanged()
    }
}