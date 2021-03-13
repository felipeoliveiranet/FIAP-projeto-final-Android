package com.francisco.geovane.marcello.felipe.projetofinalandroid.utils

import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import android.text.TextUtils
import java.util.*

@Suppress("DEPRECATION")
class LocaleUtils {

    companion object {

        fun updateLanguage(ctx: Context) {

            val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)
            val lang = prefs.getString("locale_override", "en")

            updateLanguage(ctx, lang)
        }

        fun updateLanguage(ctx: Context, lang: String?) {
            val cfg = Configuration()

            if (!TextUtils.isEmpty(lang))
                cfg.locale = Locale(lang)
            else
                cfg.locale = Locale.getDefault()

            ctx.resources.updateConfiguration(cfg, null)
        }
    }
}
