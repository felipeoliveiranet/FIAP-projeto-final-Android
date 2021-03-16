package com.francisco.geovane.marcello.felipe.projetofinalandroid.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.francisco.geovane.marcello.felipe.projetofinalandroid.BaseActivity
import com.francisco.geovane.marcello.felipe.projetofinalandroid.BuildConfig
import com.francisco.geovane.marcello.felipe.projetofinalandroid.R
import com.francisco.geovane.marcello.felipe.projetofinalandroid.utils.AnalyticsUtils
import com.francisco.geovane.marcello.felipe.projetofinalandroid.utils.LocaleUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.NavigationMenu
import com.google.firebase.analytics.FirebaseAnalytics
import java.util.*


class MainActivity : BaseActivity() {

    private lateinit var analytics: FirebaseAnalytics

    private var bundle: Bundle = Bundle()
    private var appId: String = BuildConfig.APP_ID
    private var pageId: String = "Main"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //Analytics
        analytics = FirebaseAnalytics.getInstance(this)
        AnalyticsUtils.setPageData(analytics, bundle, appId, pageId)

        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_map, R.id.navigation_list, R.id.navigation_about, R.id.navigation_logout))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
   }
}