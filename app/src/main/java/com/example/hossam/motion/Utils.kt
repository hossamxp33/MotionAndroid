package com.example.hossam.motion

import android.app.Application
import android.content.res.Configuration
import android.os.Build
import android.view.ContextThemeWrapper
import java.util.*

object LocaleUtils {

    private var sLocale: Locale? = null

    fun setLocale(locale: Locale) {
        sLocale = locale
        if (sLocale != null) {
            Locale.setDefault(sLocale)
        }
    }

    fun updateConfig(wrapper: ContextThemeWrapper) {
        if (sLocale != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val configuration = Configuration()
            configuration.setLocale(sLocale)
            wrapper.applyOverrideConfiguration(configuration)
        }
    }

    fun updateConfig(app: Application, configuration: Configuration) {
        if (sLocale != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //Wrapping the configuration to avoid Activity endless loop
            val config = Configuration(configuration)
            // We must use the now-deprecated config.locale and res.updateConfiguration here,
            // because the replacements aren't available till API level 24 and 17 respectively.
            config.locale = sLocale
            val res = app.baseContext.resources
            res.updateConfiguration(config, res.displayMetrics)
        }
    }
}