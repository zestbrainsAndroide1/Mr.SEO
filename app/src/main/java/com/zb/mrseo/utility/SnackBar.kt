package com.zb.moodlist.utility

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class SnackBar {

    companion object {

        fun show(activity: Activity?, msg: String) {

            if (activity != null) {
                Snackbar
                    .make(activity.window.decorView.findViewById(android.R.id.content), validateString(msg), Snackbar.LENGTH_LONG).show()

            }
        }
        /*
        * if you are passing context from some where then it will be show toast because snackbar can show only for activities and view
        * */
        fun show(activity: Context?, msg: String) {

            if (activity != null) {
                if (activity is Activity) {
                    show(activity, msg)
                } else {
                    Toast.makeText(activity, validateString(msg), Toast.LENGTH_LONG).show()
                }
            }
        }

        // for activity and action
        fun show(activity: Activity?, msg: String, actionText: String, clickListener: View.OnClickListener) {
            if (activity != null) {
                Snackbar
                    .make(activity.window.decorView.findViewById(android.R.id.content), validateString(msg), Snackbar.LENGTH_LONG)
                    .setAction(actionText, clickListener).show()
            }

        }


        // for view and action
        fun show(view: View?, msg: String, actionText: String, clickListener: View.OnClickListener) {
            if (view != null) {
                Snackbar
                    .make(view, validateString(msg), Snackbar.LENGTH_LONG)
                    .setAction(actionText, clickListener).show()
            }

        }


        // for styling view and action color action
        fun show(mContext: Activity?, error : Boolean, snackBarMsg: String, isAction : Boolean, actionText: String, clickListener: View.OnClickListener?) {
            if (mContext != null) {
                val snackbar = Snackbar.make(mContext.window.decorView.findViewById(android.R.id.content), validateString(snackBarMsg), Snackbar.LENGTH_LONG)
                val snackbarView : View = snackbar.view

                // styling for rest of text

                if (error) {
                    val textView: TextView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text)
                    textView.setTextColor(Color.WHITE)
                    textView.textSize = 14f
//                    textView.typeface = ResourcesCompat.getFont(mContext, R.font.poppins_regular)
                    snackbarView.setBackgroundColor(Color.RED)
                }
                if (isAction){
                    snackbar.setActionTextColor(Color.WHITE)
                    snackbar.setAction(actionText, clickListener)
                }
                snackbar.show()

            }

        }

        private fun validateString(msg: String?): String {
            return msg ?: "null"
        }
    }

}