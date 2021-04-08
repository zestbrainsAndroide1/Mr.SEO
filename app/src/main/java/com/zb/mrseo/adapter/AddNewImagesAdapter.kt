package com.zb.mrseo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide


import com.zb.moodlist.utility.gone
import com.zb.moodlist.utility.setSafeOnClickListener
import com.zb.moodlist.utility.visible
import com.zb.mrseo.R
import com.zb.mrseo.interfaces.OnItemSelectListener
import com.zb.mrseo.utility.AddProductImage
import java.util.*

class AddNewImagesAdapter(private val context: Context, private val addProductImages: ArrayList<AddProductImage>,
                          private val onClick: OnItemSelectListener
) : RecyclerView.Adapter<AddNewImagesAdapter.MyHolder>() {
    var selected = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_img, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        if (position == addProductImages.size) {
            holder.mAddImage.gone()

/*
            holder.ivVillaImage.gone()
*/
/*
            holder.imgClose.gone()
*/

        }
        else {
            holder.mAddImage.gone()

            holder.ivVillaImage.visible()
/*
            holder.imgClose.visible()
*/
            val addProductImage = addProductImages[position]
            if (addProductImage.uri != null) {
                Glide.with(context)
                    .load(addProductImage.uri)
                    .into(holder.ivVillaImage)
            }
        }

        holder.mAddImage.setSafeOnClickListener { onClick.OnItemSelectListener(1,"Add") }

    }

    override fun getItemCount(): Int {
        return addProductImages.size
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivVillaImage: ImageView = itemView.findViewById(R.id.img)
        val mAddImage: ImageView = itemView.findViewById(R.id.img_upload)
/*
        var imgClose: androidx.appcompat.widget.AppCompatImageView = itemView.findViewById(R.id.img_close)
*/
    }

}