package com.zb.mrseo.adapter

import android.app.Activity
import com.zb.mrseo.activity.LoginActivity
import com.zb.mrseo.activity.ProductActivity




import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zb.mrseo.R


class HomeAdapter(private val mActivity: Context
) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var llMain: LinearLayout = itemView.findViewById(R.id.ll_home)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_home, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {
        holder.llMain.setOnClickListener(View.OnClickListener {

            val intent = Intent(mActivity, ProductActivity::class.java)
            (mActivity as Activity).startActivity(intent)
            (mActivity as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        })

    }

    override fun getItemCount(): Int {
        return 6
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