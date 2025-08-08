package com.helloumi.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val picture: String,
    val gender: String,
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String
)