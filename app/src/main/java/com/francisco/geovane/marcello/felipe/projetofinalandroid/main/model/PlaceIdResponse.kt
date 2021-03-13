package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PlaceIdResponse (
    val candidates: List<Candidate>,
    val status: String
)

@Parcelize
data class Candidate (
    @SerializedName("place_id")
    val place_id: String
) : Parcelable