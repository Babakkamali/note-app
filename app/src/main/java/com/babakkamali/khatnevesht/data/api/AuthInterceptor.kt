package com.babakkamali.khatnevesht.data.api

import android.content.Context
import android.content.Intent
import com.babakkamali.khatnevesht.exception.NoConnectivityException
import com.babakkamali.khatnevesht.utils.ContextProvider
import com.babakkamali.khatnevesht.utils.NetworkUtils
import com.babakkamali.khatnevesht.utils.PreferenceUtils
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val context = ContextProvider.getContext()
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw NoConnectivityException()
        }

        // Check if the request URL contains "/note/"
        if (originalRequest.url().encodedPath().contains("/note/")) {
            val token = PreferenceUtils.getToken(context)

            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
            val response = chain.proceed(newRequest)

            // Check for 401 Unauthorized response
            if (response.code() == 401) {
                PreferenceUtils.removeToken(context)
                ContextProvider.getContext().sendBroadcast(Intent("ACTION_LOGOUT").setPackage(context.packageName))
            }

            return response
        }

        return chain.proceed(originalRequest)
    }
}
