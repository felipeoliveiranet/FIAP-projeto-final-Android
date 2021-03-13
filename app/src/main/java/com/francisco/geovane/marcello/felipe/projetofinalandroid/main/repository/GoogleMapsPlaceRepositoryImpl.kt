package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.repository

import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.model.PlaceDetailsResponse
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.model.PlaceIdResponse
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.service.GoogleMapsPlaceService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleMapsPlaceRepositoryImpl(var service: GoogleMapsPlaceService) : GoogleMapsPlaceRepository {

    override fun getPlaceId(
        key: String,
        address: String,
        onComplete: (PlaceIdResponse?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        service.getPlaceId(key, address).enqueue(object : Callback<PlaceIdResponse> {

            override fun onFailure(call: Call<PlaceIdResponse>, t: Throwable) { onError(t) }
            override fun onResponse(call: Call<PlaceIdResponse>, response: Response<PlaceIdResponse>) {
                if(response.isSuccessful && response.body()?.status.equals("OK", true))
                    onComplete(response.body())
                else
                    onError(Throwable("getPlaceId -> Request failed!"))
            }
        })
    }

    override fun getPlaceDetailsById(
        key: String,
        place_id: String,
        onComplete: (PlaceDetailsResponse?) -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        service.getPlaceDetailsById(key, place_id).enqueue(object : Callback<PlaceDetailsResponse> {

            override fun onFailure(call: Call<PlaceDetailsResponse>, t: Throwable) { onError(t) }
            override fun onResponse(call: Call<PlaceDetailsResponse>, response: Response<PlaceDetailsResponse>) {
                if(response.isSuccessful && response.body()?.status.equals("OK", true))
                    onComplete(response.body())
                else
                    onError(Throwable("getPlaceDetails -> Request failed!"))
            }
        })
    }
}
