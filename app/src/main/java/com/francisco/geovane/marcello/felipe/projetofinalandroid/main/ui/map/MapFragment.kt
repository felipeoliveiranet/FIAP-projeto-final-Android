package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.francisco.geovane.marcello.felipe.projetofinalandroid.BuildConfig
import com.francisco.geovane.marcello.felipe.projetofinalandroid.R
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.repository.GoogleMapsPlaceRepositoryImpl
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.service.GoogleMapsPlaceService
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.service.GoogleMapsRequestApi
import com.francisco.geovane.marcello.felipe.projetofinalandroid.utils.AnalyticsUtils
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapFragment : Fragment() {
    
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var key: String

    private var LOG_TAG = "DEBUG"
    private var bundle: Bundle = Bundle()
    private var appId: String = BuildConfig.APP_ID
    private var pageId: String = "Map"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_map, container, false)

        // Remote Config
        fetchRemoteConfig(inflater, container)

        // Analytics
        analytics = FirebaseAnalytics.getInstance(context)
        AnalyticsUtils.setPageData(analytics, bundle, appId, pageId)

        return root
    }

    private fun fetchRemoteConfig(inflater: LayoutInflater, container: ViewGroup?) {
        val root = inflater.inflate(R.layout.fragment_map, container, false)
        val btn: Button = root.findViewById(R.id.btn_search)
        val textAddress: EditText = root.findViewById(R.id.text_address)

        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
            fetchTimeoutInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetch(0)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "onComplete Succeeded")
                    remoteConfig.activate()
                    key = remoteConfig.getString("google_maps_api_key")

                    btn.setOnClickListener {
                        searchAddress(root, key, textAddress.text.toString())
                    }

                    btn.performClick()
                } else {
                    Log.d(LOG_TAG, "onComplete failed")
                }
            }
            .addOnFailureListener { e -> Log.d(LOG_TAG, "onFailure : " + e.message) }
    }

    private fun searchAddress(root: View, key: String, address: String) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = GoogleMapsRequestApi.setClient(client)
        val service = retrofit.create(GoogleMapsPlaceService::class.java)
        val repo = GoogleMapsPlaceRepositoryImpl(service)

        val txtName: TextView = root.findViewById(R.id.text_name)
        val txtCategory: TextView = root.findViewById(R.id.text_category)
        val txtInfo: TextView = root.findViewById(R.id.text_info)

        repo.getPlaceDetailsById(key, address, {
            val detail = it?.results?.first()
            val categories = detail?.types
            val lat = detail?.geometry?.location?.lat
            val long = detail?.geometry?.location?.lng
            val name = detail?.name
            var cats = ""

            if (categories != null) {
                for (cat in categories) {
                    cats += "$cat; "
                }
            }

            txtName.text = name
            txtCategory.text = cats
            txtInfo.text = lat.toString() + "\n" + long.toString()
        }, {
            Log.d(LOG_TAG, it?.localizedMessage.toString())
        })
    }
}