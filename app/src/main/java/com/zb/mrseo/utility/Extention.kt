package com.zb.moodlist.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import com.clipper_hub.utility.SafeClickListener
import com.zb.mrseo.utility.MyCustomProgressDialog

inline fun <E : Any, T : Collection<E>> T?.withNotNullNorEmpty(func: T.() -> Unit) {
    if (this != null && this.isNotEmpty()) {
        with(this) { func() }
    }
}

inline fun <E : Any, T : Collection<E>> T?.whenNotNullNorEmpty(func: (T) -> Unit) {
    if (this != null && this.isNotEmpty()) {
        func(this)
    }
}





/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * To get desired value for given VALUE whether it is null or not
 * Usage :
 *      any_kind_of_value.ifNotNullOrElse({ your_return_value_if_given_value_is_not_null }, { your_return_value_if_given_value_is_null })
 * */

inline fun <T : Any, R> T?.ifNotNullOrElse(ifNotNullPath: (T) -> R, elsePath: () -> R) =
        let { if (it == null) elsePath() else ifNotNullPath(it) }


/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * To visible and gone view
 * Usage :
 *      to Visible view : yourView.visible()
 *      to Gone view : yourView.gone()
 *      to Invisible view : yourView.invisible()
 * */
fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}


/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * To startActivity in short manner
 * @param T is your activity
 * @param params is your put extras
 * Usage :
 *      start<YourActivity>(
"string_key" to "string_value",
"int_key" to 12
)
 * */

inline fun <reified T : Activity> Context.start(vararg params: Pair<String, Any>) {
    val intent = Intent(this, T::class.java).apply {
        params.forEach {
            when (val value = it.second) {
                is Int -> putExtra(it.first, value)
                is String -> putExtra(it.first, value)
                is Double -> putExtra(it.first, value)
                is Float -> putExtra(it.first, value)
                else -> throw IllegalArgumentException("Wrong param type!")
            }
            return@forEach
        }
    }
    startActivity(intent)

}


/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * To get string from Edit text directly
 * Usage :
 *      yourEditText.asString()
 * */
fun AppCompatEditText.asString(): String {
    return text.toString().trim()
}

fun AppCompatTextView.asString(): String {
    return text.toString().trim()
}
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * Alert dialog
 * Usage :
 *    showAlertDialog {
 *         setTitle("Greet")
 *         setMessage("Welcome again, want coffee?")
 *         positiveButton("Yes") {}
 *         negativeButton {}
 *    }
 * */
fun Context.showAlertDialog(dialogBuilder: AlertDialog.Builder.() -> Unit) {
    val builder = AlertDialog.Builder(this)
    builder.dialogBuilder()
    val dialog = builder.create()

    dialog.show()
}

fun AlertDialog.Builder.positiveButton(text: String, handleClick: (which: Int) -> Unit = {}) {
    this.setPositiveButton(text) { _, which -> handleClick(which) }
}

fun AlertDialog.Builder.negativeButton(text: String, handleClick: (which: Int) -> Unit = {}) {
    this.setNegativeButton(text) { _, which -> handleClick(which) }
}


/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * Inflate view in adapter
 *
 * Usage :
 *      1.) parent.inflate(R.layout.my_layout) -> default root is false
 *      2.) parent.inflate(R.layout.my_layout, true)
 * */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}


/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * Click listener to stop multi click on view
 * */
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}


/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * To check whether network is available or not
 * */
fun Context.isNetworkAvailable(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = cm.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnectedOrConnecting
}


/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * To save the value into preference
 * Usage :
 *
 * prefSave :
 *      prefSave("key_value_string" to "value")
 *      prefSave("key_value_integer" to 12)
 *
 * prefContain :
 *      prefContain("key")
 *
 * */
fun Context.prefSave(pair: Pair<String, Any>) = Prefs.setValue(this, pair.first, pair.second)

fun Context.prefGetString(key: String, defaultValue: Any) = Prefs.getValue(
    this,
    key,
    defaultValue.toString()
)!!
fun Context.prefGetInt(key: String, defaultValue: Any) =
    Prefs.getValue(this, key, (defaultValue as Int))

fun Context.prefGetObject(key: String, defaultValue: String, c: Class<*>) =
    Prefs.getObject(this, key, defaultValue, c)

fun Context.prefContain(key: String) = Prefs.contain(this, key)

fun Context.prefRemove(key: String) = Prefs.removeValue(this, key)

fun Context.prefClearAll() = Prefs.clearAllData(this)


/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * To get Color, Resource, String from xml or res folder
 * Usage :
 *
 * getStr :
 *      getStr(R.string.your_string)
 *
 * getRes :
 *      getRes(R.drawable.your_drawable)
 *
 * getClr :
 *      getClr(R.color.your_color)
 *
 * getDimen :
 *      getDimen(R.dimen.your_dimension)
 *
 * */

fun Context.getStr(id: Int)  = resources.getString(id)

fun Context.getRes(id: Int) = ResourcesCompat.getDrawable(resources, id, theme)!!

fun Context.getClr(id: Int) = ResourcesCompat.getColor(resources, id, theme)

fun Context.getDimen(id: Int) = resources.getDimension(id)


/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/
/*------------------------------------------------------------------------------------------------*/

/**
 * To hide keyboard
 * Usage :
 *      hideKeyboard()
 *
 * */




fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


fun getProgressDialog(context: Context): ProgressDialog {
    val myCustomProgressDialog = MyCustomProgressDialog(context)

    try {

        myCustomProgressDialog.isIndeterminate = true
        myCustomProgressDialog.setCancelable(false)
        myCustomProgressDialog.show()
        return myCustomProgressDialog

    }catch (e:Exception){
        return myCustomProgressDialog

    }


//    val myCustomProgressDialog = MyCustomProgressDialog(context)
//    myCustomProgressDialog.isIndeterminate = true
//    myCustomProgressDialog.setCancelable(false)
//    myCustomProgressDialog.show()
//    return myCustomProgressDialog

}

fun dismissDialog(context: Context, mProgressDialog: ProgressDialog) {
    try {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    } catch (e: Exception) {

    }

}










@SuppressLint("ShowToast")
fun ShowToast(response: String,activity: Activity) {
    Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
}