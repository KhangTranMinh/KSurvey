package com.ktm.ksurvey.presentation.util

import com.ktm.ksurvey.presentation.viewmodel.AuthViewModel

class Logger(private val clazz: String) {

    companion object {
        private const val TAG = "KSurvey"

        fun get(): Logger = Logger("Logger")

        fun get(clazz: String): Logger = Logger(clazz)
    }


    fun log(message: String) {
        android.util.Log.d(TAG, "$clazz | $message")
    }

    fun log(message: String, throwable: Throwable) {
        android.util.Log.e(TAG, "$clazz | $message", throwable)
    }
}

fun AuthViewModel.log(message: String) {
    Logger.get(this::class.java.simpleName).log(message)
}

