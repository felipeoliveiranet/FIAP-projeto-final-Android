package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.service

import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.model.PlaceDetailsResponse
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.model.PlaceIdResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsPlaceService {

    @GET("/maps/api/place/findplacefromtext/json")
    fun getPlaceId(@Query("key") apiKey: String, @Query("input") address: String, @Query("inputtype") type: String = "textquery", @Query("fields") fields: String = "place_id"): Call<PlaceIdResponse>

    @GET("/maps/api/place/textsearch/json")
    fun getPlaceDetailsById(@Query("key") apiKey: String, @Query("input") input: String): Call<PlaceDetailsResponse>
}