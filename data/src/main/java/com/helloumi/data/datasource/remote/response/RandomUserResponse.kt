package com.helloumi.data.datasource.remote.response

import com.google.gson.annotations.SerializedName

data class RandomUserResponse(
    @SerializedName("results")
    val results: List<RandomUserResult>,
    @SerializedName("info")
    val info: RandomUserInfo
)

data class RandomUserResult(
    @SerializedName("gender")
    val gender: String,
    @SerializedName("name")
    val name: RandomUserName,
    @SerializedName("location")
    val location: RandomUserLocation,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("picture")
    val picture: RandomUserPicture
)

data class RandomUserName(
    @SerializedName("title")
    val title: String,
    @SerializedName("first")
    val first: String,
    @SerializedName("last")
    val last: String
)

data class RandomUserLocation(
    @SerializedName("street")
    val street: RandomUserStreet,
    @SerializedName("city")
    val city: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("postcode")
    val postcode: String
)

data class RandomUserStreet(
    @SerializedName("number")
    val number: Int,
    @SerializedName("name")
    val name: String
)

data class RandomUserPicture(
    @SerializedName("large")
    val large: String,
    @SerializedName("medium")
    val medium: String,
    @SerializedName("thumbnail")
    val thumbnail: String
)

data class RandomUserInfo(
    @SerializedName("seed")
    val seed: String,
    @SerializedName("results")
    val results: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("version")
    val version: String
)
