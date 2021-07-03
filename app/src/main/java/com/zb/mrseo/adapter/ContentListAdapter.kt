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
import com.zb.mrseo.model.PlatformListModel
import de.hdodenhof.circleimageview.CircleImageView


class ContentListAdapter(
    private val mActivity: Context,
    val onPlatformClick: OnPlatformClick
) :
    RecyclerView.Adapter<ContentListAdapter.MyViewHolder>() {
    private var mModel = ArrayList<MyPostsModel.Datum>()


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var tvCategoryName: TextView = itemView.findViewById(R.id.tv_category_name)
        var tvKeyword: TextView = itemView.findViewById(R.id.tv_content_title)
        var tvKeywordTitle: TextView = itemView.findViewById(R.id.tv_content_keyword)
        var tvTypeName: TextView = itemView.findViewById(R.id.tv_shopping_mall_name)
        var tvTitle: TextView = itemView.findViewById(R.id.tv_name)
        var tvTotalPoints: TextView = itemView.findViewById(R.id.tv_total)
        var tvRemainingPoints: TextView = itemView.findViewById(R.id.tv_remaining_point)
        var llTop: LinearLayout = itemView.findViewById(R.id.ll_top)
        var llKeyword: LinearLayout = itemView.findViewById(R.id.ll_keyword)
        var cvHome: CardView = itemView.findViewById(R.id.cv_home)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_content, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {

        holder.tvCategoryName.text = mModel[listPosition].title.toString()
        holder.tvTotalPoints.text = mModel[listPosition].registerPoint.toString()
/*
        holder.tvRemainingPoints.text = mModel[listPosition].registerPoint!!.minus(mModel[listPosition].helperCount!!).toString()
*/


/*
        holder.tvRemainingPoints.text = mModel[listPosition].registerPoint!!.minus(mModel[listPosition].helperCount!!).toString()+"/"
*/
try{
    var point=mModel[listPosition].helperCount!!.toInt() - mModel[listPosition].helperUser!!.toInt()
    holder.tvRemainingPoints.text = point.toString()

}catch (e:Exception){

}


        holder.tvTitle.text = mModel[listPosition].name.toString()

        if(mModel[listPosition].keyword.toString().equals("")){
            holder.llKeyword.gone()
        }else{
            holder.llKeyword.visible()
            holder.tvKeywordTitle.text = mModel[listPosition].keyword.toString()

        }

        if(mModel[listPosition].categoryId.toString().equals("1")){
            holder.tvTypeName.text = mActivity.getString(R.string.shopping_mall_name)

        }else if(mModel[listPosition].categoryId.toString().equals("2")){
            holder.tvTypeName.text = mActivity.getString(R.string.shopping_mall_name)

        }else if(mModel[listPosition].categoryId.toString().equals("3")){
            holder.tvTypeName.text = mActivity.getString(R.string.blog_name)

        }else if(mModel[listPosition].categoryId.toString().equals("4")){
            holder.tvTypeName.text = mActivity.getString(R.string.cafe_name)

        }else{

        }


        try {


            holder.llTop.setBackgroundColor(Color.parseColor(mModel[listPosition].color.toString()))

        } catch (e: Exception) {

        }

        holder.cvHome.setOnClickListener(View.OnClickListener {
            val intent = Intent(mActivity, HelpActivity::class.java)
            intent.putExtra("id", mModel[listPosition].postId.toString())
            intent.putExtra("category", mModel[listPosition].categoryId.toString())
            (mActivity as Activity).startActivity(intent)
            (mActivity as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            (mActivity as Activity).finish()
        })
    }

    override fun getItemCount(): Int {
        return mModel.size
    }

    fun addAll(mData: ArrayList<MyPostsModel.Datum>?) {
        mModel.addAll(mData!!)
        notifyItemInserted(mModel.size - 1)
        notifyDataSetChanged()
    }

    fun clear() {
        mModel.clear()
        notifyDataSetChanged()
    }
}