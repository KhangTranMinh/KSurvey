package com.ktm.ksurvey.data.util

import com.ktm.ksurvey.data.repository.SurveyRepositoryImpl
import com.ktm.ksurvey.data.repository.UserRepositoryImpl
import com.ktm.ksurvey.data.storage.SurveyStore
import com.ktm.ksurvey.data.storage.UserStore

class Logger(private val clazz: String) {

    companion object {
        private const val TAG = "KSurvey"

        fun get(): Logger = Logger("Logger")

        fun get(clazz: String): Logger = Logger(clazz)
    }


    fun log(message: String) {
        android.util.Log.d(TAG, "$clazz | $message")
    }

    fun log(message: String, throwable: Throwable?) {
        android.util.Log.e(TAG, "$clazz | $message", throwable)
    }
}

fun UserRepositoryImpl.log(message: String) {
    Logger.get(this::class.java.simpleName).log(message)
}

fun SurveyRepositoryImpl.log(message: String) {
    Logger.get(this::class.java.simpleName).log(message)
}

fun SurveyStore.log(message: String) {
    Logger.get(this::class.java.simpleName).log(message)
}