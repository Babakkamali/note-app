package com.babakkamali.khatnevesht.utils

import android.content.Context

object ContextProvider {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun getContext(): Context {
        if (!::context.isInitialized) {
            throw IllegalStateException("ContextProvider is not initialized. Call init() first.")
        }
        return context
    }
}