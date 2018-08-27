package s18alg.myapplication

import android.content.Context
import android.net.ConnectivityManager

fun checkConnection(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return cm.activeNetworkInfo?.isConnected == true
}