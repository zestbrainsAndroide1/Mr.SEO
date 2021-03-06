package com.zb.moodlist.utility

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.zb.mrseo.utility.MyCustomProgressDialog

import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

private val TAG = "Util"

//fun getProgressDialog(context: Context): KProgressHUD? {
//    /*val myCustomProgressDialog = MyCustomProgressDialog(context)
//    myCustomProgressDialog.isIndeterminate = true
//    myCustomProgressDialog.setCancelable(false)
//    myCustomProgressDialog.show()*/
//    return KProgressHUD.create(context)
//            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//            .setCancellable(false)
//            .setAnimationSpeed(2)
//            .setDimAmount(0.5f)
//            .show()
//
//}

//fun dismissDialog(context: Context, mProgressDialog: KProgressHUD) {
//    try {
//        if (mProgressDialog.isShowing) {
//            mProgressDialog.dismiss()
//        }
//    } catch (e: Exception) {
//
//    }
//
//}

@SuppressLint("showToast")
fun showToast(response: String, activity: Activity) {
    Toast.makeText(activity.applicationContext, response, Toast.LENGTH_SHORT).show()
}
fun openSoftKeyboard(context: Context) {
    try {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    } catch (e: NullPointerException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

fun <T> ImageView.loadCircularImage(
    model: T,
    borderSize: Float = 0F,
    borderColor: Int = Color.WHITE
) {

    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    Glide.with(context)
        .asBitmap()
        .load(model)
        .placeholder(circularProgressDrawable)
        .centerCrop()
        .apply(RequestOptions.circleCropTransform())
        .into(object : BitmapImageViewTarget(this) {
            override fun setResource(resource: Bitmap?) {
                setImageDrawable(
                    resource?.run {
                        RoundedBitmapDrawableFactory.create(
                            resources,
                            if (borderSize > 0) {
                                createBitmapWithBorder(borderSize, borderColor)
                            } else {
                                this
                            }
                        ).apply {
                            isCircular = true
                        }
                    }
                )
            }
        })
}

fun Bitmap.createBitmapWithBorder(borderSize: Float, borderColor: Int = Color.WHITE): Bitmap {
    val borderOffset = (borderSize * 2).toInt()
    val halfWidth = width / 2
    val halfHeight = height / 2
    val circleRadius = Math.min(halfWidth, halfHeight).toFloat()
    val newBitmap = Bitmap.createBitmap(
        width + borderOffset,
        height + borderOffset,
        Bitmap.Config.ARGB_8888
    )

    // Center coordinates of the image
    val centerX = halfWidth + borderSize
    val centerY = halfHeight + borderSize

    val paint = Paint()
    val canvas = Canvas(newBitmap).apply {
        // Set transparent initial area
        drawARGB(0, 0, 0, 0)
    }

    // Draw the transparent initial area
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
    canvas.drawCircle(centerX, centerY, circleRadius, paint)

    // Draw the image
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, borderSize, borderSize, paint)

    // Draw the createBitmapWithBorder
    paint.xfermode = null
    paint.style = Paint.Style.STROKE
    paint.color = borderColor
    paint.strokeWidth = borderSize
    canvas.drawCircle(centerX, centerY, circleRadius, paint)
    return newBitmap
}



fun getNavBarHeight(mContext: Activity): Int {

    if (!hasNavBar(mContext)) {
        return 0
    }

    val resourceId = mContext.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
        mContext.resources.getDimensionPixelSize(resourceId)
    } else 0
}

internal fun hasNavBar(context: Context): Boolean {
    val decorView = (context as Activity).window.decorView
    var has = false
    decorView.setOnSystemUiVisibilityChangeListener { visibility ->
        if (visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0) {
            // TODO: The navigation bar is visible. Make any desired
            // adjustments to your UI, such as showing the action bar or
            // other navigational controls.
            has = true

        }
    }
    return has
}

fun getStatusBarHeight(context: Activity): Int {
    var result = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo

    return activeNetworkInfo != null
}

fun distanceCount(number: Float): String {
    return when {
//        number > 10000 -> "NA km"
        number >= 1000 -> "%.1f".format(number / 1000f).plus("k KM")
        else -> "%.1f".format(number).plus(" KM")
    }
}

fun currencyCount(number: Float): String {
    val v = NumberFormat.getCurrencyInstance()
    return when {
        number > 999 && number < 1000000 -> "%.1f".format(number / 1000f).plus("K")
        number > 99 -> "%.1f".format(number / 1000f).plus("k km")
        else -> "%.1f".format(number).plus(" km")
    }
}

fun View.fadeIn(millis: Long) {
    /* Animate fade in to give better feel instead of visibility View.VISIBLE */
    this.visible()
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), 0, 100)
    colorAnimation.duration = millis // milliseconds
    colorAnimation.addUpdateListener {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.alpha = (it.animatedValue.toString().toFloat()) / 100f
        }
    }
    colorAnimation.start()
}

fun View.fadeOut(millis: Long) {
    /* Animate fade in to give better feel instead of visibility View.VISIBLE */
    this@fadeOut.visible()
    val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), 100, 0)
    colorAnimation.duration = millis // milliseconds
    colorAnimation.addUpdateListener {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.alpha = (it.animatedValue.toString().toFloat()) / 100f
        }
    }
    colorAnimation.addListener(object : Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
//            this@fadeOut.gone()
        }
    })
    colorAnimation.start()
}

fun View.scaleIn(millis: Long) {
    val anim = ScaleAnimation(
            0f, 1f, // Start and end values for the X axis scaling
            0f, 1f, // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f) // Pivot point of Y scaling
    anim.fillAfter = true // Needed to keep the result of the animation
    anim.duration = millis
    this.startAnimation(anim)
}

fun View.scaleInFromRight(millis: Long) {
    val anim = ScaleAnimation(
            1f, 1f, // Start and end values for the X axis scaling
            1f, 1f, // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f) // Pivot point of Y scaling
    anim.fillAfter = true // Needed to keep the result of the animation
    anim.duration = millis
    this.startAnimation(anim)
}

fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val theta = lon1 - lon2
    var dist = sin(deg2rad(lat1)) * sin(deg2rad(lat2)) + (cos(deg2rad(lat1))
            * cos(deg2rad(lat2))
            * cos(deg2rad(theta)))
    dist = acos(dist)
    dist = rad2deg(dist)
    dist *= 60.0 * 1.1515
    return dist
}

private fun deg2rad(deg: Double): Double {
    return deg * Math.PI / 180.0
}

private fun rad2deg(rad: Double): Double {
    return rad * 180.0 / Math.PI
}

/*make call and pass activity and mobile number*/



fun gettime(originalString: String): Date? {
    var date: Date? = null
    try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        date = simpleDateFormat.parse(originalString)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return date
}

fun getClr(mContext: Context, id: Int): Int {
    return ResourcesCompat.getColor(mContext.resources, id, null)
}
fun getStr(mContext: Context, id: Int): String {
    return mContext.resources.getString(id)
}

fun getRes(mContext: Activity, id: Int): Drawable? {
    return ResourcesCompat.getDrawable(mContext.resources, id, mContext.theme)!!
}

fun getRes(mContext: Context, id: Int): Drawable? {
    return ResourcesCompat.getDrawable(mContext.resources, id, mContext.theme)!!
}


fun getDate(originalString: String): String {
    var newstr = ""
    try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = simpleDateFormat.parse(originalString)
        newstr = SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(date!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return newstr
}

fun getDatePost(originalString: String): String {
    var newstr = ""
    try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = simpleDateFormat.parse(originalString)
        newstr = SimpleDateFormat("dd MMM yy", Locale.getDefault()).format(date!!)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return newstr
}
fun dismissDialog(context: Context, mProgressDialog: MyCustomProgressDialog) {
    try {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    } catch (e: Exception) {

    }

}

fun Int.dp(): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()
}





