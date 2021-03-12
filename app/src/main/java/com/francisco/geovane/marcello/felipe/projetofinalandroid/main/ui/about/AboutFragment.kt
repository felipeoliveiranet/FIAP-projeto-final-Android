package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.ui.about

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.francisco.geovane.marcello.felipe.projetofinalandroid.BuildConfig
import com.francisco.geovane.marcello.felipe.projetofinalandroid.R
import com.google.firebase.analytics.FirebaseAnalytics

class AboutFragment : Fragment() {

    private var bundle: Bundle = Bundle()
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        analytics = FirebaseAnalytics.getInstance(context)

        var version: String = "0.0.1"

        var appId: String = BuildConfig.APP_ID
        var pageId: String = "about"

        bundle.clear()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "$appId:$pageId")
        analytics?.logEvent("e_PageView", bundle)

        try {

            val pInfo: PackageInfo = context!!.packageManager.getPackageInfo(context!!.packageName, 0)
            var arr: List<String> = pInfo.applicationInfo.packageName.split(".")

            version = pInfo.versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        val root = inflater.inflate(R.layout.fragment_about, container, false)

        val appName: TextView = root.findViewById(R.id.text_app_name)
        appName.setText(R.string.app_name)

        val appVersion: TextView = root.findViewById(R.id.text_app_version_number)
        appVersion.setText(version)

        val list: ListView = root.findViewById(R.id.list_text_app_by_info)

        val arr = listOf(
                "Felipe Oliveira   - RM 338405",
                "Francisco Olvera  - RM 338050",
                "Geovane Medina    - RM 338045",
                "Marcello Chuahy   - RM 337780"
        )

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(root.context, R.layout.list_item, R.id.text_info,  arr)
        list.setAdapter(arrayAdapter)

        return root
    }
}