package com.zb.mrseo.interfaces

import android.widget.TextView

interface OnStatusClick {
    fun onCashSentClick(position:Int,title:String,id:String)
    fun onViewProofClick(position:Int,title:String,id:String)
    fun onFinishedClick(position:Int,title:String,id:String)
    fun onChatClick(position:Int,title:String,id:String)

}