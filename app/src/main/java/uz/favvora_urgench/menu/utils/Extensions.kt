package uz.favvora_urgench.menu.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar


fun toast(view: View, msg: String) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    Log.d("FROM TOAST", "MESSAGE: --->   $msg")
}

@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

fun String.setAsPrice(): String {
    var result = ""
    for (i in 1..this.length) {
        val ch: Char = this[this.length - i]
        if (i % 3 == 1 && i > 1) {
            result = ".$result"
        }
        result = ch.toString() + result
    }

    return result
}

fun Activity.setImageHeight(image: ImageView) {
    //Get Screen width programmatically
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    val widthPixels = displayMetrics.widthPixels

    //Change pixel to dp
    val heightInDp = (widthPixels / resources.displayMetrics.density).toInt()

    //Set layout with programmatically
    val params = image.layoutParams
    params.height = heightInDp
    image.layoutParams = params
}

fun Activity.showWarningDialog(
    title: String,
    msg: String,
    ok_text: String,
    ok: (() -> Unit),
    cancel_text: String,
    cancel: (() -> Unit)
) {
    val dialog = AlertDialog.Builder(this)
    dialog.setTitle(title)
    dialog.setMessage(msg)
    dialog.setCancelable(true)
    dialog.setPositiveButton(ok_text) { _, _ ->
        ok.invoke()
    }
    dialog.setNegativeButton(cancel_text) { _, _ ->
        cancel.invoke()
    }
    dialog.create()
    dialog.show()
}