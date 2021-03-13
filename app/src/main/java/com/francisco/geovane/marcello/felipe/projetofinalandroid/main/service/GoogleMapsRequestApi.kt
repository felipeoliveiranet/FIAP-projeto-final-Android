package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.service

import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.repository.GoogleMapsPlaceRepositoryImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GoogleMapsRequestApi {

    companion object {

        fun setClient(okHttpClient: OkHttpClient): Retrofit {

            return Retrofit.Builder().client(okHttpClient)
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
