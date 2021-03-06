package com.clipper_hub.utility

import android.content.Context
import android.widget.Toast

class ShowToast {

    /*
         * This included because, sonar raise create bug each class should have constructor
         * */
    companion object {


        var toast: Toast? = null

        fun show(context: Context?, msg: String) {

            if (context != null) {
                if (toast != null) {
                    toast!!.cancel()
                }
                toast = Toast.makeText(context.applicationContext, validateString(msg), toastTime)
                toast!!.show()
            }
        }


        private fun validateString(msg: String?): String {
            return msg ?: "null"
        }


        private val toastTime: Int
            get() = Toast.LENGTH_SHORT
    }

}