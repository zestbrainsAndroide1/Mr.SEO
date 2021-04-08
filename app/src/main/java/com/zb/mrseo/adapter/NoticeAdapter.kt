package com.zb.mrseo.adapter

import android.app.Activity


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zb.moodlist.utility.gone
import com.zb.moodlist.utility.visible
import com.zb.mrseo.R
import com.zb.mrseo.activity.*
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.HomeModel
import com.zb.mrseo.model.MyPostsModel
import com.zb.mrseo.model.NoticeModel
import com.zb.mrseo.model.PlatformListModel
import de.hdodenhof.circleimageview.CircleImageView


class NoticeAdapter(
    private val mActivity: Context) :
    RecyclerView.Adapter<NoticeAdapter.MyViewHolder>() {
    private var mModel = ArrayList<NoticeModel.Datum>()
   var row_index=-1

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {





        var tvName: TextView = itemView.findViewById(R.id.tv_name)
        var tvDate: TextView = itemView.findViewById(R.id.tv_date)
        var tvDesc: TextView = itemView.findViewById(R.id.tv_desc)
        var imgProduct: ImageView = itemView.findViewById(R.id.img_product)
        var imgDown: ImageView = itemView.findViewById(R.id.img_down)
        var imgUp: ImageView = itemView.findViewById(R.id.img_up)
        var clTop: ConstraintLayout = itemView.findViewById(R.id.cl_top)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_notice, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {

     holder.tvName.text = mModel[listPosition].title.toString()
        holder.tvDesc.text = mModel[listPosition].description.toString()
        holder.tvDate.text = mModel[listPosition].createdAt.toString()
 Glide.with(mActivity).load(mModel[listPosition].image.toString()).into(holder.imgProduct)


        holder.clTop.setOnClickListener(View.OnClickListener {

            if (holder.imgDown.visibility == View.VISIBLE) {
                holder.imgDown.gone()
                holder.imgUp.visible()

                holder.tvDesc.visible()
                holder.imgProduct.visible()
                row_index=listPosition
                notifyDataSetChanged()
            } else if (holder.imgUp.visibility == View.VISIBLE) {
                holder.imgUp.gone()
                holder.imgDown.visible()
                holder.tvDesc.gone()
                holder.imgProduct.gone()

            }
        })
        if(row_index==listPosition){
            holder.imgProduct.visible()
            holder.tvDesc.visible()
        }else{
            holder.imgProduct.gone()
            holder.tvDesc.gone()

        }

    }

    override fun getItemCount(): Int {
        return mModel.size
    }

    fun addAll(mData: ArrayList<NoticeModel.Datum>?) {
        mModel.addAll(mData!!)
        notifyItemInserted(mModel.size - 1)
        notifyDataSetChanged()
    }

    fun clear() {
        mModel.clear()
        notifyDataSetChanged()
    }
}