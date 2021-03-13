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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class MapFragment : Fragment() {

    private var bundle: Bundle = Bundle()
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var remoteConfig: FirebaseRemoteConfig

    private var appId: String = BuildConfig.APP_ID
    private var pageId: String = "Map"

    private lateinit var mapViewModel: MapViewModel

    private val key = "AIzaSyD-3gjRiQfyaE4beBdjZJmBGYlkqITqxtc"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        remoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()

        analytics = FirebaseAnalytics.getInstance(context)
        AnalyticsUtils.setPageData(analytics, bundle, appId, pageId)

        val root = inflater.inflate(R.layout.fragment_map, container, false)

        /*
        mapViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        */

        val btn: Button = root.findViewById(R.id.btn_search)
        val text_address: EditText = root.findViewById(R.id.text_address)

        btn.setOnClickListener {

            searchAddress(root, text_address.text.toString())
        }

        btn.performClick()

        return root
    }

    private fun searchAddress(
        root: View,
        address: String
    ) {
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

            var detail = it?.results?.first()
            var categories = detail?.types
            var lat = detail?.geometry?.location?.lat
            var long = detail?.geometry?.location?.lng
            var name = detail?.name
            var cats: String = ""

            if (categories != null) {

                for (cat in categories) {

                    cats += cat + "; "
                }
            }

            txtName.text = name
            txtCategory.text = cats
            txtInfo.text = lat.toString() + "\n" + long.toString()

        }, {
            Log.e("PLACE_DETAILS", it?.localizedMessage.toString())
        })
    }
}