@file:Suppress("unused")

package com.francisco.geovane.marcello.felipe.projetofinalandroid

import android.app.Application
import com.francisco.geovane.marcello.felipe.projetofinalandroid.utils.LocaleUtils

class LocaleDefault : Application() {

    override fun onCreate() {

        LocaleUtils.updateLanguage(this)
        super.onCreate()
    }
}
