package com.zb.mrseo.adapter

import android.app.Activity


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView
import com.zb.moodlist.utility.gone
import com.zb.moodlist.utility.invisible
import com.zb.moodlist.utility.visible
import com.zb.mrseo.R
import com.zb.mrseo.activity.*
import com.zb.mrseo.interfaces.OnPlatformClick
import com.zb.mrseo.interfaces.OnStatusClick
import com.zb.mrseo.model.*
import de.hdodenhof.circleimageview.CircleImageView


class HelperDetailAdapter(
    private val mActivity: Context,
    val onPlatformClick: OnStatusClick,
    var postId:String
) :
    RecyclerView.Adapter<HelperDetailAdapter.MyViewHolder>() {
    private var mModel = ArrayList<MyPostDetailModel.Data.Helper>()

    companion object {
        var btnCashSent: AppCompatButton? = null
        var btnViewProof: AppCompatButton? = null
        var btnFinish: AppCompatButton? = null
        var btnGoToChat: AppCompatButton? = null

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var tvId: TextView = itemView.findViewById(R.id.tv_email)
        var tvBankName: TextView = itemView.findViewById(R.id.tv_bank_name)
        var tvAccNo: TextView = itemView.findViewById(R.id.tv_bank_acc_no)
        var tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        var imgFirstProduct: PorterShapeImageView = itemView.findViewById(R.id.img_product)
        var imgSecondProduct: PorterShapeImageView = itemView.findViewById(R.id.img_first_product)
        var cvConfirm: CardView = itemView.findViewById(R.id.cvConfirm)
        var llPost: LinearLayout = itemView.findViewById(R.id.ll_post)

        var btnCashSent: AppCompatButton = itemView.findViewById(R.id.btn_cash_sent)
        var btnViewProof: AppCompatButton = itemView.findViewById(R.id.btn_view_proofs)
        var btnFinish: AppCompatButton = itemView.findViewById(R.id.btn_finished)
        var btnGoToChat: AppCompatButton = itemView.findViewById(R.id.btn_chat)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_post, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {

        holder.tvId.text = mModel[listPosition].email.toString()
        holder.tvStatus.text = mModel[listPosition].status.toString()
        holder.tvBankName.text = mModel[listPosition].bankName.toString()
        holder.tvAccNo.text = mModel[listPosition].accountNumber.toString()



        holder.imgFirstProduct.setOnClickListener(View.OnClickListener {

            val intent = Intent(mActivity, ViewImageActivity::class.java)
            intent.putExtra("url",mModel[listPosition].proofImage!!.get(0).file)
            intent.putExtra("id",postId)

            (mActivity as Activity).startActivity(intent)
            (mActivity as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            (mActivity as Activity).finish()
        })
        holder.imgSecondProduct.setOnClickListener(View.OnClickListener {
            val intent = Intent(mActivity, ViewImageActivity::class.java)
            intent.putExtra("url",mModel[listPosition].proofImage!!.get(1).file)
            intent.putExtra("id",postId)

            (mActivity as Activity).startActivity(intent)
            (mActivity as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
            (mActivity as Activity).finish()

        })

        holder.btnCashSent.setOnClickListener(View.OnClickListener {
            btnCashSent = holder.btnCashSent
            btnViewProof = holder.btnViewProof

            onPlatformClick.onCashSentClick(
                listPosition,
                mModel[listPosition].helpId.toString(),
                "cash_sent"
            )

        })
        holder.btnViewProof.setOnClickListener(View.OnClickListener {

            if (mModel[listPosition].status.toString().equals("finished")) {
                holder.llPost.visible()
                if (mModel[listPosition].proofImage!!.size == 1) {
                    Glide.with(mActivity).load(mModel[listPosition].proofImage!!.get(0).file)
                        .into(holder.imgFirstProduct)
                    holder.imgSecondProduct.invisible()
                } else if (mModel[listPosition].proofImage!!.size == 2) {
                    Glide.with(mActivity).load(mModel[listPosition].proofImage!!.get(0).file)
                        .into(holder.imgFirstProduct)

                    Glide.with(mActivity).load(mModel[listPosition].proofImage!!.get(1).file)
                        .into(holder.imgSecondProduct)
                    holder.imgFirstProduct.visible()
                    holder.imgSecondProduct.visible()
                } else {
                    holder.imgFirstProduct.gone()
                    holder.imgSecondProduct.gone()

                }
            } else if (mModel[listPosition].status.toString().equals("checking_proofs")) {
                holder.llPost.visible()
                if (mModel[listPosition].proofImage!!.size == 1) {
                    Glide.with(mActivity).load(mModel[listPosition].proofImage!!.get(0).file)
                        .into(holder.imgFirstProduct)
                    holder.imgSecondProduct.invisible()
                } else if (mModel[listPosition].proofImage!!.size == 2) {
                    Glide.with(mActivity).load(mModel[listPosition].proofImage!!.get(0).file)
                        .into(holder.imgFirstProduct)

                    Glide.with(mActivity).load(mModel[listPosition].proofImage!!.get(1).file)
                        .into(holder.imgSecondProduct)
                    holder.imgFirstProduct.visible()
                    holder.imgSecondProduct.visible()
                } else {
                    holder.imgFirstProduct.gone()
                    holder.imgSecondProduct.gone()

                }
            } else {
                btnViewProof = holder.btnViewProof
                btnFinish = holder.btnFinish

                holder.llPost.visible()
                holder.cvConfirm.visible()
                if (mModel[listPosition].proofImage!!.size == 1) {
                    Glide.with(mActivity).load(mModel[listPosition].proofImage!!.get(0).file)
                        .into(holder.imgFirstProduct)
                    holder.imgSecondProduct.invisible()
                } else if (mModel[listPosition].proofImage!!.size == 2) {
                    Glide.with(mActivity).load(mModel[listPosition].proofImage!!.get(0).file)
                        .into(holder.imgFirstProduct)

                    Glide.with(mActivity).load(mModel[listPosition].proofImage!!.get(1).file)
                        .into(holder.imgSecondProduct)
                    holder.imgFirstProduct.visible()
                    holder.imgSecondProduct.visible()
                } else {
                    holder.imgFirstProduct.gone()
                    holder.imgSecondProduct.gone()

                }
                if (mModel[listPosition].proofImage!!.size <= 0) {
                    holder.llPost.gone()
                }


            }

        })
        holder.cvConfirm.setOnClickListener(View.OnClickListener {

            onPlatformClick.onViewProofClick(
                listPosition,
                mModel[listPosition].helpId.toString(),
                "checking_proofs"
            )


        })
        holder.btnFinish.setOnClickListener(View.OnClickListener {
            btnFinish = holder.btnFinish
            onPlatformClick.onFinishedClick(
                listPosition,
                mModel[listPosition].helpId.toString(),
                "finished"
            )


        })
        holder.btnGoToChat.setOnClickListener(View.OnClickListener {
            btnGoToChat = holder.btnGoToChat
            onPlatformClick.onChatClick(
                listPosition,
                mModel[listPosition].helpId.toString(),
                mModel[listPosition].threadId.toString()
            )

        })

        if (mModel[listPosition].status.toString().equals("cash_sent")) {
            holder.btnCashSent!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4EA3FE")));
            holder.btnCashSent.isEnabled = false
            holder.btnViewProof.isEnabled = true
        }

        if (mModel[listPosition].status.toString().equals("checking_proofs")) {
            holder.btnCashSent!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4EA3FE")));
            holder.btnViewProof!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6CCF71")));
            holder.btnViewProof.isEnabled = false
            holder.btnFinish.isEnabled = true
        }

        if (mModel[listPosition].status.toString().equals("finished")) {
            holder.btnCashSent!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4EA3FE")));
            holder.btnViewProof!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6CCF71")));
            holder.btnFinish!!.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4EA3FE")));

            holder.btnCashSent.isEnabled = false

            holder.btnViewProof.isEnabled = true
            holder.btnFinish.isEnabled = false
        }

    }

    override fun getItemCount(): Int {
        return mModel.size
    }

    fun addAll(mData: ArrayList<MyPostDetailModel.Data.Helper>?) {
        mModel.addAll(mData!!)
        notifyItemInserted(mModel.size - 1)
        notifyDataSetChanged()
    }

    fun clear() {
        mModel.clear()
        notifyDataSetChanged()
    }
}