package com.babakkamali.khatnevesht.data.api

import android.content.Context
import com.babakkamali.khatnevesht.utils.ContextProvider
import com.babakkamali.khatnevesht.utils.PreferenceUtils
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Check if the request URL contains "/note/"
        if (originalRequest.url().encodedPath().contains("/note/")) {
            val token = PreferenceUtils.getToken(ContextProvider.getContext())

            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            return chain.proceed(newRequest)
        }

        return chain.proceed(originalRequest)
    }
}
