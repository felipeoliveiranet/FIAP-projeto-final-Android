package com.francisco.geovane.marcello.felipe.projetofinalandroid.main.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class PlaceDetailsResponse (
    val results: List<Result>,
    val status: String
)

@Parcelize
data class Result (
    @SerializedName("formatted_address")
    val formattedAddress: String,

    @SerializedName("formatted_phone_number")
    val formattedPhoneNumber: String,

    val name: String,
    val geometry: Geometry,
    val icon: String,
    val photos: List<Photo>,

    @SerializedName("place_id")
    val placeID: String,

    val types: List<String>
) : Parcelable

@Parcelize
data class Geometry (
    val location: Location,
    val viewport: Viewport
) : Parcelable

@Parcelize
data class Location (
    val lat: Double,
    val lng: Double
) : Parcelable

@Parcelize
data class Viewport (
    val northeast: Location,
    val southwest: Location
) : Parcelable

@Parcelize
data class Photo (
    val height: Long,

    @SerializedName("html_attributions")
    val htmlAttributions: List<String>,

    @SerializedName("photo_reference")
    val photoReference: String,

    val width: Long
) : Parcelable
