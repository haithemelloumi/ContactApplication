package com.helloumi.data.mapper

import com.helloumi.data.datasource.remote.response.RandomUserLocation
import com.helloumi.data.datasource.remote.response.RandomUserName
import com.helloumi.data.datasource.remote.response.RandomUserPicture
import com.helloumi.data.datasource.remote.response.RandomUserResult
import com.helloumi.data.datasource.remote.response.RandomUserStreet
import com.helloumi.domain.model.ContactDomain
import com.helloumi.domain.model.LocationDomain
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Unit tests for ContactMapper.
 */
class ContactMapperTest {

    @Test
    fun `WHEN call toCityEntity THEN ensure result`() {
        // GIVEN
        val randomUserResult = RandomUserResult(
            gender = "male",
            name = RandomUserName(
                title = "title",
                first = "first",
                last = "last"
            ),
            location = RandomUserLocation(
                street = RandomUserStreet(
                    number = 1,
                    name = "street General de Gaulle"
                ),
                city = "Paris",
                state = "NY",
                country = "France",
                postcode = "10001"
            ),
            email = "email",
            phone = "phone",
            picture = RandomUserPicture(
                large = "large",
                medium = "medium",
                thumbnail = "thumbnail"
            )
        )

        val contactDomain = ContactDomain(
            id = "firstlastemail".hashCode().toString(),
            firstName = "first",
            lastName = "last",
            email = "email",
            phone = "phone",
            picture = "large",
            gender = "male",
            location = LocationDomain(
                street = "1 street General de Gaulle",
                city = "Paris",
                state = "NY",
                country = "France",
                postcode = "10001"
            )
        )

        // WHEN
        val result = randomUserResult.toContactDomain()

        // THEN
        assertEquals(contactDomain, result)
    }
}
