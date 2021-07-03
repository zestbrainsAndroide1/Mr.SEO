package com.zb.mrseo.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zb.moodlist.utility.*
import com.zb.mrseo.R
import com.zb.mrseo.activity.PostDetailActivity
import com.zb.mrseo.model.ApplyModel
import com.zb.mrseo.model.HomeModel
import com.zb.mrseo.model.LoginModel
import com.zb.mrseo.restapi.*
import kotlinx.android.synthetic.main.activity_post_detail.*


class HomeAdapter(
    private val mActivity: Context
) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>(), ApiResponseInterface {
    private var mModel = ArrayList<HomeModel.Data>()
    var mUserModel: LoginModel.Data? = null
    var postId: String = ""
    var categoryId : String = ""

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

            if(mModel[listPosition].categoryId.toString().equals("1") || mModel[listPosition].categoryId.toString().equals("2")){
                holder.tvUserName.text = mModel[listPosition].mallName.toString()

            }else  if(mModel[listPosition].categoryId.toString().equals("3")){
                holder.tvUserName.text = mModel[listPosition].blogName.toString()

            }else  if(mModel[listPosition].categoryId.toString().equals("4")){
                holder.tvUserName.text = mModel[listPosition].cafeName.toString()

            }else{

            }
            holder.tvCategory.text = mModel[listPosition].title.toString()
            holder.llTop.setBackgroundColor(Color.parseColor(mModel[listPosition].color.toString()))

/*
            holder.tvPoints.text = mModel[listPosition].registerPoint!!.minus(mModel[listPosition].helperCount!!.toInt()).toString()
*/


/*
            holder.tvPoints.text = mModel[listPosition].registerPoint!!.minus(mModel[listPosition].helperCount!!.toInt()).toString() + "/" + mModel[listPosition].registerPoint.toString()
*/

            try{
                var point=mModel[listPosition].helperCount!!.toInt() - mModel[listPosition].helperUser!!.toInt()
                holder.tvPoints.text = point.toString()

            }catch (e:Exception){

            }



        } catch (e: Exception) {

        }
        mUserModel = Prefs.getObject(
            mActivity,
            AppConstant.ACCOUNT_DATA, "", LoginModel.Data::class.java
        ) as LoginModel.Data


        holder.cvHome.setOnClickListener(View.OnClickListener {
            postId = mModel[listPosition].postId.toString()

            val intent = Intent(mActivity, PostDetailActivity::class.java)
            intent.putExtra("id", postId)
            intent.putExtra("helpId", mModel[listPosition].postId.toString())

            (mActivity as Activity).startActivity(intent)
            (mActivity as Activity).overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        })

        /** 광고 클릭 시 **/
        holder.imgAdd.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(mModel[listPosition].advertisements?.url.toString())
            (mActivity as Activity).startActivity(intent)
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

    private fun applyForHelp(postId: String) {
        if (isNetworkAvailable(mActivity)) {

            ApiRequest<Any>(
                activity = mActivity as Activity,
                objectType = ApiInitialize.initialize()
                    .applyForHelp(

                        "Bearer ".plus(mUserModel!!.token.toString()),
                        postId
                    ),
                TYPE = WebConstant.APPLY_FOR_HELP,
                isShowProgressDialog = true,
                apiResponseInterface = this@HomeAdapter
            )

        } else {
            SnackBar.show(
                mActivity as Activity,
                true,
                getStr(mActivity as Activity, R.string.str_network_error),
                false,
                "OK",
                null
            )
        }
    }

    override fun getApiResponse(apiResponseManager: ApiResponseManager<*>) {
        when (apiResponseManager.type) {

            WebConstant.APPLY_FOR_HELP -> {
                val response = apiResponseManager.response as ApplyModel

                when (response.status) {
                    200 -> {


                        val intent = Intent(mActivity, PostDetailActivity::class.java)
                        intent.putExtra("id", postId)
                        intent.putExtra("detail", response.data)
                        (mActivity as Activity).startActivity(intent)
                        (mActivity as Activity).overridePendingTransition(
                            R.anim.slide_in_right,
                            R.anim.slide_out_left
                        )

                    }
                    else -> ShowToast(response.message!!, mActivity as Activity)
                }
            }


        }
    }

}