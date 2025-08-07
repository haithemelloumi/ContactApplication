package com.helloumi.data.mapper

import com.helloumi.data.datasource.remote.response.RandomUserResult
import com.helloumi.domain.model.ContactDomain
import com.helloumi.domain.model.LocationDomain

fun RandomUserResult.toContactDomain(): ContactDomain {
    return ContactDomain(
        id = "${name.first}${name.last}${email}".hashCode().toString(),
        firstName = name.first,
        lastName = name.last,
        email = email,
        phone = phone,
        picture = picture.large,
        gender = gender,
        location = LocationDomain(
            street = "${location.street.number} ${location.street.name}",
            city = location.city,
            state = location.state,
            country = location.country,
            postcode = location.postcode
        )
    )
}
