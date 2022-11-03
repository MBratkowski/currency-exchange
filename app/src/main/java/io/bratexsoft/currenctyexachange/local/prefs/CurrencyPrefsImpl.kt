package io.bratexsoft.currenctyexachange.local.prefs

import android.content.SharedPreferences

class CurrencyPrefsImpl(
    private val sharedPreferences: SharedPreferences
) : CurrencyPrefs {

    companion object {
        private const val FIRST_LAUNCH_KEY = "firstLaunchKey"
    }

    override fun isFirstLaunch() = sharedPreferences.getBoolean("firstLaunchKey", true)

    override fun updateFirstLaunch() {
        sharedPreferences.edit().apply {
            putBoolean(FIRST_LAUNCH_KEY, false)
        }.apply()
    }
}