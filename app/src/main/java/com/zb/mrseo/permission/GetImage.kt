package com.zb.mindme.permission

import android.net.Uri

interface GetImage {
    fun getImage(_uri : Uri, _profilePath : String)
    fun onCancel()
    fun onError(_profilePath : String)
}