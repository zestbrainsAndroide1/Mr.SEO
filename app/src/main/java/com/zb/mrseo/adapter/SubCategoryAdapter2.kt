package com.zb.mrseo.adapter

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
import com.zb.mrseo.interfaces.OnCategoryClick
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.model.CategoryListModel


class SubCategoryAdapter2(private val mActivity: Context,private val mModel: ArrayList<CategoryListModel.Data.Category.SubCategory>, val onCategoryClick: OnCategoryClick) :
    RecyclerView.Adapter<SubCategoryAdapter2.MyViewHolder>() {

/*
    private var mModel = ArrayList<CategoryListModel.Data.Category.SubCategory>()
*/

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var llMain: LinearLayout = itemView.findViewById(R.id.ll1)
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var v1:View = itemView.findViewById(R.id.v1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_platform, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {

        holder.tvTitle.text = mModel[listPosition]?.name

        if(listPosition==mModel.size-1){
            holder.v1.visibility=View.GONE
        }else{
            holder.v1.visibility=View.VISIBLE

        }

        holder.llMain.setOnClickListener {

            onCategoryClick.onSubCategoryClick2(
                listPosition,
                mModel[listPosition]?.name.toString(),
                mModel[listPosition]?.id.toString())
        }


    }

    override fun getItemCount(): Int {
        return mModel.size
    }

    /*fun addAll(mData: ArrayList<CategoryListModel.Data.Category.SubCategory>?) {
        mModel.addAll(mData!!)
        notifyItemInserted(mModel.size - 1)
        notifyDataSetChanged()
    }

    fun clear() {
        mModel.clear()
        notifyDataSetChanged()
    }*/
}