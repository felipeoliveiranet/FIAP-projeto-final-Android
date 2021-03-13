package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.repository

import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.model.PlaceDetailsResponse
import com.francisco.geovane.marcello.felipe.projetofinalandroid.main.model.PlaceIdResponse

interface GoogleMapsPlaceRepository {

    fun getPlaceId(
        key: String,
        address: String,
        onComplete: (PlaceIdResponse?) -> Unit,
        onError: (Throwable?) -> Unit
    )

    fun getPlaceDetailsById(
        key: String,
        place_id: String,
        onComplete: (PlaceDetailsResponse?) -> Unit,
        onError: (Throwable?) -> Unit
    )
}
