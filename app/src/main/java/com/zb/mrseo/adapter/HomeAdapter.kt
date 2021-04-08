package com.zb.mrseo.adapter

import android.app.Activity
import com.zb.mrseo.activity.LoginActivity
import com.zb.mrseo.activity.ProductActivity


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinpermissions.ifNotNullOrElse
import com.zb.moodlist.utility.gone
import com.zb.moodlist.utility.ifNotNullOrElse
import com.zb.moodlist.utility.visible
import com.zb.mrseo.R
import com.zb.mrseo.activity.PostDetailActivity
import com.zb.mrseo.model.HomeModel
import com.zb.mrseo.model.PlatformListModel
import de.hdodenhof.circleimageview.CircleImageView


class HomeAdapter(
    private val mActivity: Context
) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    private var mModel = ArrayList<HomeModel.Data>()


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var cvHome: CardView = itemView.findViewById(R.id.cv_home)
        var tvUserName: TextView = itemView.findViewById(R.id.tv_user_name)
        var tvPoints: TextView = itemView.findViewById(R.id.tv_points)
        var tvCategory: TextView = itemView.findViewById(R.id.tv_category_name)
        var imgAdd: ImageView = itemView.findViewById(R.id.img_advertisement)

        var llTop: LinearLayout = itemView.findViewById(R.id.ll_top)


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


        /*if(listPosition%5 == 0){
            holder.imgAdd.visible()
        }*/

        try {

            if (mModel[listPosition].advertisements?.image != null) {
                holder.imgAdd.visible()
                Glide.with(mActivity).load(mModel[listPosition].advertisements!!.image.toString())
                    .into(holder.imgAdd)
            } else {
                Glide.with(mActivity).load(mModel[listPosition].advertisements!!.image.toString())
                    .into(holder.imgAdd)

                holder.imgAdd.gone()

            }
        } catch (e: Exception) {
            holder.imgAdd.gone()

        }



            try {
                holder.tvCategory.text = mModel[listPosition].title.toString()

                holder.llTop.setBackgroundColor(Color.parseColor(mModel[listPosition].color.toString()))
                holder.tvUserName.text = mModel[listPosition].email.toString()


                holder.tvPoints.text =
                    mModel[listPosition].helperCount.toString() + "/" + mModel[listPosition].registerPoint.toString()

            } catch (e: Exception) {

            }

        holder.cvHome.setOnClickListener(View.OnClickListener {

            val intent = Intent(mActivity, PostDetailActivity::class.java)
            intent.putExtra("id", mModel[listPosition].postId.toString())
            (mActivity as Activity).startActivity(intent)
            (mActivity as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        })

    }

    override fun getItemCount(): Int {
        return mModel.size
    }

    fun addAll(mData: ArrayList<HomeModel.Data>?) {
        mModel.addAll(mData!!)
        notifyItemInserted(mModel.size - 1)
        notifyDataSetChanged()
    }

    fun clear() {
        mModel.clear()
        notifyDataSetChanged()
    }
}