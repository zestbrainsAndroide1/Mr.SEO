package com.zb.mrseo.adapter

import com.bumptech.glide.Glide
import com.zb.mrseo.model.MyHelpDetailModel


import com.zb.mrseo.model.BankListModel


import com.zb.mrseo.R
import com.zb.mrseo.model.PlatformListModel


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView
import com.zb.mrseo.interfaces.OnPlatformClick


class ImageAdapter(private val mActivity: Context) :
    RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    private var mModel = ArrayList<MyHelpDetailModel.Datum.ProofImage>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        var img: PorterShapeImageView = itemView.findViewById(R.id.img)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_upload, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {


       Glide.with(mActivity).load(mModel[listPosition].file.toString()).into(holder.img)


    }

    override fun getItemCount(): Int {
        return mModel.size
    }

    fun addAll(mData: ArrayList<MyHelpDetailModel.Datum.ProofImage>?) {
        mModel.addAll(mData!!)
        notifyItemInserted(mModel.size - 1)
        notifyDataSetChanged()
    }

    fun clear() {
        mModel.clear()
        notifyDataSetChanged()
    }
}