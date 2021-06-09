package com.example.android.utils

import android.util.Log

/**
 */
object Lg {

    @JvmStatic
    fun d(TAG: String, message: String) {
        if (C.IS_APP_DEBUGGABLE)
            Log.d(TAG, message)
    }

    @JvmStatic
    fun i(TAG: String, message: String) {
        if (C.IS_APP_DEBUGGABLE)
            Log.i(TAG, message)
    }

    @JvmStatic
    fun i(message: String) {
        i(C.APP_NAME, message)
    }

    @JvmStatic
    fun w(TAG: String, message: String) {
        if (C.IS_APP_DEBUGGABLE)
            Log.w(TAG, message)
    }

    @JvmStatic
    fun v(TAG: String, message: String) {
        if (C.IS_APP_DEBUGGABLE)
            Log.v(TAG, message)
    }

    @JvmStatic
    fun e(TAG: String, message: String) {
        if (C.IS_APP_DEBUGGABLE)
            Log.e(TAG, message)
    }

    @JvmStatic
    fun e(TAG: String, message: String, exception: Throwable) {
        if (C.IS_APP_DEBUGGABLE)
            Log.e(TAG, message, exception)
    }
}