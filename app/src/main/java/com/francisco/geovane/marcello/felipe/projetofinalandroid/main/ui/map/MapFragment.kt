package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.ui.map

import android.content.Context.INPUT_METHOD_SERVICE
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.francisco.geovane.marcello.felipe.projetofinalandroid.BuildConfig
import com.francisco.geovane.marcello.felipe.projetofinalandroid.R
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.repository.GoogleMapsPlaceRepositoryImpl
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.service.GoogleMapsPlaceService
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.service.GoogleMapsRequestApi
import com.francisco.geovane.marcello.felipe.projetofinalandroid.utils.AnalyticsUtils
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener {
    
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var key: String

    private var LOG_TAG = "DEBUG"
    private var bundle: Bundle = Bundle()
    private var appId: String = BuildConfig.APP_ID
    private var pageId: String = "Map"

    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    private val defaultAddress = LatLng(-23.5641095, -46.65240989999999)
    private val defaultZoom = 16F

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_map, container, false)

        loadMap(root, savedInstanceState)

        // Remote Config
        fetchRemoteConfig(root)

        // Analytics
        analytics = FirebaseAnalytics.getInstance(context)
        AnalyticsUtils.setPageData(analytics, bundle, appId, pageId)

        return root
    }

    private fun fetchRemoteConfig(root: View) {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                Log.d(LOG_TAG, "onComplete Succeeded: $task.result")
                searchTrigger(root)

            } else { Log.d(LOG_TAG, "onComplete failed") }
        }.addOnFailureListener { e -> Log.d(LOG_TAG, "onFailure : " + e.message) }
    }

    private fun searchTrigger(root: View) {
        val btnSearch: Button = root.findViewById(R.id.btn_search)
        val btnReset: Button = root.findViewById(R.id.btn_reset)
        val textAddress: EditText = root.findViewById(R.id.text_address)

        key = remoteConfig.getString("google_maps_api_key")

        btnSearch.setOnClickListener {
            if(textAddress.text.toString().isEmpty()) {
                Toast.makeText(context, R.string.txt_fill_address, Toast.LENGTH_LONG).show()
            } else {
                searchHideKeyboard()
                searchAddress(key, textAddress.text.toString())
            }
        }

        btnReset.setOnClickListener {
            searchHideKeyboard()
            textAddress.setText("")
            updateMap(defaultAddress,"FIAP")
        }
    }

    private fun searchHideKeyboard() {
        if (requireActivity().currentFocus != null) {
            val inputManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken,0)
        }
    }

    private fun searchAddress(key: String, address: String) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        val retrofit = GoogleMapsRequestApi.setClient(client)
        val service = retrofit.create(GoogleMapsPlaceService::class.java)
        val repo = GoogleMapsPlaceRepositoryImpl(service)

        // FLAVOR
        val flavor = appId

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

            Log.i("CATS", cats)
            updateMap(LatLng(lat!!, long!!), name!!)

        }, {
            Log.d(LOG_TAG, it?.localizedMessage.toString())
        })
    }

    private fun loadMap(root: View, savedInstanceState: Bundle?) {

        mapView = root.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume() // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.localizedMessage.toString())
        }

        mapView.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true

        updateMap(defaultAddress, "FIAP" )

        map.setOnMarkerDragListener(this)
        map.setOnMapClickListener(this)
    }

    override fun onMarkerDragStart(movedPoint: Marker) { }

    override fun onMarkerDrag(movedPoint: Marker) { }

    override fun onMarkerDragEnd(movedPoint: Marker) { updateMap(LatLng(movedPoint.position.latitude, movedPoint.position.longitude)) }

    override fun onMapClick(clickedPoint: LatLng) { updateMap(clickedPoint) }

    private fun updateMap(latlong: LatLng, name: String = "") {

        val geocoder = Geocoder(this.context)

        try {

            val addressList: List<Address> = geocoder.getFromLocation(latlong.latitude, latlong.longitude, 1)


            var placeCustomName = ""
            // Tratamento de placeName
            placeCustomName = if (name.isEmpty() && !TextUtils.isDigitsOnly(addressList[0].featureName)) {
                addressList[0].featureName
            } else if (name.isEmpty() && TextUtils.isDigitsOnly(addressList[0].featureName)){
                addressList[0].subLocality + " " + addressList[0].subThoroughfare + ", " + addressList[0].subAdminArea
            } else {
                name
            }

            val placeName: String = placeCustomName
            val address: String = addressList[0].getAddressLine(0)

            val options: MarkerOptions = MarkerOptions()
            options.position(latlong)
            options.title(placeName)
            options.draggable(true)

//            map.clear()
            map.addMarker(options).showInfoWindow()
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong, defaultZoom))

        } catch (e: IOException) {
            Log.d(LOG_TAG, e.localizedMessage.toString())
        }
    }

    override fun onResume() {

        super.onResume()
        mapView.onResume()
    }
}