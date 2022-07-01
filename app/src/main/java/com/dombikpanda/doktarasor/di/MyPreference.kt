package com.dombikpanda.doktarasor.di

import android.content.Context

val PREFERENCE_NAME = "SharedPreferenceExample"
val PREFERENCE_LANGUAGE = "Language"

class MyPreference(context: Context) {
    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getLanguageCount(): String? {
        return preference.getString(PREFERENCE_LANGUAGE, "en")
    }

    fun setLanguageCount(count: String) {
        val editor = preference.edit()
        editor.putString(PREFERENCE_LANGUAGE, count)
        editor.apply()
    }
}