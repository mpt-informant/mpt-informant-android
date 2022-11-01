package me.kofesst.android.mptinformant.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import okhttp3.CacheControl
import okhttp3.Interceptor

@Suppress("DEPRECATION") // Использование deprecated для старых версий API
fun Context.hasNetwork(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = manager.activeNetwork ?: return false
        val capabilities = manager.getNetworkCapabilities(network) ?: return false
        when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    } else {
        manager.activeNetworkInfo?.isConnected == true
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OfflineInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OnlineInterceptor

fun buildOnlineInterceptor(context: Context) = Interceptor { chain ->
    val request = chain.request()
    val cacheControl = if (context.hasNetwork()) {
        CacheControl.Builder()
            .maxAge(1, TimeUnit.MINUTES)
    } else {
        CacheControl.Builder()
            .onlyIfCached()
            .maxStale(30, TimeUnit.DAYS)
    }.build()
    val response = chain.proceed(request)
    response.newBuilder()
        .removeHeader("Pragma")
        .removeHeader("Cache-Control")
        .header("Cache-Control", cacheControl.toString())
        .build()
}

fun buildOfflineInterceptor(context: Context) = Interceptor { chain ->
    var request = chain.request()
    val cacheControl = if (context.hasNetwork()) {
        CacheControl.Builder()
            .maxAge(1, TimeUnit.MINUTES)
    } else {
        CacheControl.Builder()
            .onlyIfCached()
            .maxStale(30, TimeUnit.DAYS)
    }.build()
    request = request.newBuilder()
        .removeHeader("Pragma")
        .removeHeader("Cache-Control")
        .header("Cache-Control", cacheControl.toString())
        .build()
    chain.proceed(request)
}
