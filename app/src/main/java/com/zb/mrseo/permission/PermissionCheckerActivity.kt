package com.zb.mrseo.permission

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zb.mindme.permission.BaseActivity
import com.zb.mindme.permission.PermissionChecker
import com.zb.mrseo.R


import java.util.*

class PermissionCheckerActivity : BaseActivity() {

    private val listPermissionsNeeded = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_checker)

        when {
            checkAndRequestPermissions() -> {
                PermissionChecker.permissionStatus.allGranted()
                finish()
                overridePendingTransition(0, android.R.anim.fade_out)
            }
            else -> ActivityCompat.requestPermissions(mContext, listPermissionsNeeded.toTypedArray(), PermissionChecker.REQ_PERMISSION_CODE)
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        listPermissionsNeeded.clear()
        PermissionChecker.allPermission.filterTo(listPermissionsNeeded) { ContextCompat.checkSelfPermission(mContext, it) != PackageManager.PERMISSION_GRANTED }
        return listPermissionsNeeded.isEmpty()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionChecker.REQ_PERMISSION_CODE -> {
                val perm = grantResults.map { it == PackageManager.PERMISSION_GRANTED }
                when {
                    !perm.contains(false) -> {
                        PermissionChecker.permissionStatus.allGranted()
                        finish()
                        overridePendingTransition(0, android.R.anim.fade_out)
                    }
                    else -> {
                        val perm1 = ArrayList<Boolean>()
                        listPermissionsNeeded.indices.forEach { j ->
                            when {
                                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> perm1 += shouldShowRequestPermissionRationale(listPermissionsNeeded[j])
                            }
                        }
                        when {
                            perm1.contains(true) -> {
                                /*
                                 * IF user denied for any permission then it will execute
                                 * */
                                PermissionChecker.permissionStatus.onDenied()
                                finish()
                                overridePendingTransition(0, android.R.anim.fade_out)
                            }
                            else -> {
                                /*
                                 * If user done force deny then it will execute
                                 * */
                                PermissionChecker.permissionStatus.foreverDenied()
                                finish()
                                overridePendingTransition(0, android.R.anim.fade_out)
                            }
                        }
                    }
                }
            }
        }
    }

}