package com.zb.mrseo.model


import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class DataModel(
    email: String, password: String, name: String, nickname: String, countryCode: String,
    mobile: String, bankName: String, accNo: String,otp: String,
    bankImg: String
) : Serializable {


    var email: String = email
    var password: String = password
    var name: String = name
    var nickname: String = nickname
    var countryCode: String = countryCode

    var mobile: String = mobile
    var bankName: String = bankName
    var accNo: String = accNo
    var otp: String = otp
    var bankImg: String = bankImg


}