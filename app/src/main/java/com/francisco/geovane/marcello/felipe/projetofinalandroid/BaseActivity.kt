package com.francisco.geovane.marcello.felipe.projetofinalandroid

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.francisco.geovane.marcello.felipe.projetofinalandroid.utils.LocaleUtils
import java.util.*

open class BaseActivity : AppCompatActivity() {

    private var appDefaultLocale: Locale? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        appDefaultLocale = Locale(LocaleUtils.getPrefLang(this))

        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(base: Context) {

        val contextBase = LocaleUtils.presetLocale(base!!)

        super.attachBaseContext(contextBase)
    }

    override fun onResume() {

        super.onResume()

        val lang = LocaleUtils.getPrefLang(this)

        if (appDefaultLocale != null && !appDefaultLocale.toString().equals(lang, true)) {

            recreate()
        }
    }
}