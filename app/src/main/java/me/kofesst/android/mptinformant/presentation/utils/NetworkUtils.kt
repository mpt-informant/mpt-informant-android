package me.kofesst.android.mptinformant.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import okhttp3.*

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
    val cacheControl = if (context.hasNetwork()) {
        CacheControl.Builder()
            .maxAge(1, TimeUnit.MINUTES)
    } else {
        CacheControl.Builder()
            .onlyIfCached()
            .maxStale(30, TimeUnit.DAYS)
    }.build()
    tryProceedResponse(chain) {
        chain.request()
    }.newBuilder()
        .removeHeader("Pragma")
        .removeHeader("Cache-Control")
        .header("Cache-Control", cacheControl.toString())
        .build()
}

fun buildOfflineInterceptor(context: Context) = Interceptor { chain ->
    tryProceedResponse(chain) {
        val request = chain.request()
        val cacheControl = if (context.hasNetwork()) {
            CacheControl.Builder()
                .maxAge(1, TimeUnit.MINUTES)
        } else {
            CacheControl.Builder()
                .onlyIfCached()
                .maxStale(30, TimeUnit.DAYS)
        }.build()
        request.newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}

private fun tryProceedResponse(chain: Interceptor.Chain, block: () -> Request): Response {
    val request = block()
    return try {
        chain.proceed(request)
    } catch (exception: Exception) {
        Response.Builder()
            .code(503)
            .message("Cannot connect to server")
            .body(ResponseBody.create(MediaType.get("text/plain"), "Cannot connect to server"))
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .build()
    }
}