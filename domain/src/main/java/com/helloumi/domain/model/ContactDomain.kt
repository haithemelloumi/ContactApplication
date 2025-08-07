package com.helloumi.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactDomain(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val picture: String,
    val gender: String,
    val location: LocationDomain
) : Parcelable

@Parcelize
data class LocationDomain(
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String
) : Parcelable
