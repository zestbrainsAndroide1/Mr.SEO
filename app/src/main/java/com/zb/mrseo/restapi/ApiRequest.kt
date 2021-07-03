package com.zb.mrseo.restapi


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import com.zb.moodlist.utility.dismissDialog
import com.zb.moodlist.utility.getProgressDialog
import com.zb.moodlist.utility.showToast
import com.zb.mrseo.activity.LoginActivity


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("ParcelCreator")
class ApiRequest<T>(private val activity: Activity,
                    objectType: T,
                    private val TYPE: Int,
                    private val isShowProgressDialog: Boolean,
                    private val apiResponseInterface: ApiResponseInterface
) : Callback<T>,
    Parcelable {
    private var mProgressDialog: ProgressDialog? = null
    private var retryCount = 0
    private var call: Call<T>? = null

    init {
        showProgress()
        call = objectType as Call<T>?
        if (call?.isExecuted!!) call?.cancel()
        call!!.enqueue(this)
    }

    private fun showProgress() {
        if (isShowProgressDialog) {
            mProgressDialog = getProgressDialog(activity)
        }
    }

    private fun dismissProgress() {
        if (isShowProgressDialog) {
            dismissDialog(activity, mProgressDialog!!)
        }
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        dismissProgress()
        when {
            response.code() == 203 -> { apiResponseInterface.getApiResponse(ApiResponseManager(response.body(), 203,"203")) }

            response.isSuccessful -> apiResponseInterface.getApiResponse(ApiResponseManager(response.body(), TYPE,"200"))
            response.code() == 503 -> Toast.makeText(activity, response.message().toString(), Toast.LENGTH_SHORT).show()
            response.code() == 500 -> Toast.makeText(activity, response.message().toString(), Toast.LENGTH_SHORT).show()

            else -> {
                val error = ErrorUtils.parseError(response)
                when {
                    response.code() == 401 ->
                    {
                        val mIntent = Intent(activity, LoginActivity::class.java)
                        activity.startActivity(mIntent)
                        activity.finishAffinity()
                    }



                    response.code() == 412 -> {

                        showToast(error.message!!, activity!!)


                      /*  if(TYPE== WebConstant.GET_SEARCH_DETAIL){
                            apiResponseInterface.getApiResponse(ApiResponseManager(error,TYPE,response.code().toString()))

                        }else{
                            showToast(error.message!!, activity!!)

                        }*/

                    }
                    error.message != null ->
                    {
                        if(error.message=="User not active"){
                            showToast("your account still not approved by admin", activity)
                        }else
                            showToast(error.message!!, activity)
                    }
                }
            }
        }
        Log.d("@@@@@@@@@@@@", response.toString())
    }

    override fun onFailure(call: Call<T>, error: Throwable) {
        error.printStackTrace()
        Log.e(TAG, "onFailure: " + error.message)
        if (retryCount++ < TOTAL_RETRIES) {
            Log.v(TAG, "Retrying... ($retryCount out of $TOTAL_RETRIES)")
            retry()
            return
        }
        dismissProgress()
    }

    private fun retry() {
        call!!.clone().enqueue(this)
    }

    companion object {
        private const val TAG = "ApiRequest"
        private const val TOTAL_RETRIES = 2
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(TYPE)
        parcel.writeByte(if (isShowProgressDialog) 1 else 0)
        parcel.writeInt(retryCount)
    }

    override fun describeContents(): Int {
        return 0
    }

}
