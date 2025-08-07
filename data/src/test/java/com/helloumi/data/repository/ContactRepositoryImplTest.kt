package com.helloumi.data.repository

import com.helloumi.data.datasource.local.ContactLocalDS
import com.helloumi.data.datasource.local.entity.ContactEntity
import com.helloumi.data.datasource.remote.RandomUserRemoteDS
import com.helloumi.data.datasource.remote.response.RandomUserInfo
import com.helloumi.data.datasource.remote.response.RandomUserLocation
import com.helloumi.data.datasource.remote.response.RandomUserName
import com.helloumi.data.datasource.remote.response.RandomUserPicture
import com.helloumi.data.datasource.remote.response.RandomUserResponse
import com.helloumi.data.datasource.remote.response.RandomUserResult
import com.helloumi.data.datasource.remote.response.RandomUserStreet
import com.helloumi.data.mapper.toContactDomain
import com.helloumi.domain.model.ContactDomain
import com.helloumi.domain.model.LocationDomain
import com.helloumi.domain.model.result.ContactResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for ContactRepositoryImpl.
 */
class ContactRepositoryImplTest {

    private lateinit var contactRepositoryImpl: ContactRepositoryImpl
    private lateinit var randomUserRemoteDS: RandomUserRemoteDS
    private lateinit var contactLocalDS: ContactLocalDS

    @Before
    fun setUp() {
        randomUserRemoteDS = mockk()
        contactLocalDS = mockk()
        contactRepositoryImpl = ContactRepositoryImpl(randomUserRemoteDS, contactLocalDS)
    }

    @Test
    fun `WHEN getContacts is called with success response THEN should emit Loading then Success`() =
        runTest {
            // GIVEN
            val page = 1
            val results = 20
            val mockRandomUserResponse = createMockRandomUserResponse()
            val expectedContacts = mockRandomUserResponse.results.map { it.toContactDomain() }

            coEvery {
                randomUserRemoteDS.getContacts(
                    page = page,
                    results = results
                )
            } returns mockRandomUserResponse
            coEvery { contactLocalDS.insertContacts(any()) } returns Unit

            // WHEN
            val result = contactRepositoryImpl.getContacts(page, results)

            // THEN
            val emittedValues = mutableListOf<ContactResult>()
            result.collect { emittedValues.add(it) }

            assertEquals(2, emittedValues.size) // should emit Loading and Success
            assertTrue(emittedValues[0] is ContactResult.Loading) // first value
            assertTrue(emittedValues[1] is ContactResult.Success) // second value

            val successResult = emittedValues[1] as ContactResult.Success
            assertEquals(expectedContacts.size, successResult.contacts.size)
            assertEquals(expectedContacts[0].firstName, successResult.contacts[0].firstName)
            assertEquals(expectedContacts[0].lastName, successResult.contacts[0].lastName)

            coVerify { contactLocalDS.insertContacts(any()) }
        }

    @Test
    fun `WHEN getLocalContacts is called THEN should return mapped ContactDomain list`() = runTest {
        // GIVEN
        val mockContactEntities = createMockContactEntities()
        val expectedContactDomains = mockContactEntities.map { it.toContactDomain() }

        coEvery { contactLocalDS.getAllContacts() } returns flowOf(mockContactEntities)

        // WHEN
        val result = contactRepositoryImpl.getLocalContacts()

        // THEN
        val actualContacts = result.first()
        assertEquals(expectedContactDomains.size, actualContacts.size)
        assertEquals(expectedContactDomains[0].id, actualContacts[0].id)
        assertEquals(expectedContactDomains[0].firstName, actualContacts[0].firstName)
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    // Helper methods
    private fun createMockRandomUserResponse(): RandomUserResponse {
        return RandomUserResponse(
            results = listOf(
                RandomUserResult(
                    gender = "male",
                    name = RandomUserName(
                        title = "Mr",
                        first = "John",
                        last = "Doe"
                    ),
                    location = RandomUserLocation(
                        street = RandomUserStreet(
                            number = 123,
                            name = "Main Street"
                        ),
                        city = "New York",
                        state = "NY",
                        country = "USA",
                        postcode = "10001"
                    ),
                    email = "john.doe@example.com",
                    phone = "+1234567890",
                    picture = RandomUserPicture(
                        large = "https://randomuser.me/api/portraits/men/1.jpg",
                        medium = "https://randomuser.me/api/portraits/med/men/1.jpg",
                        thumbnail = "https://randomuser.me/api/portraits/thumb/men/1.jpg"
                    )
                )
            ),
            info = RandomUserInfo(
                seed = "contactapp",
                results = 1,
                page = 1,
                version = "1.3"
            )
        )
    }

    private fun createMockContactEntities(): List<ContactEntity> {
        return listOf(
            ContactEntity(
                id = "1",
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@example.com",
                phone = "+1234567890",
                picture = "https://randomuser.me/api/portraits/men/1.jpg",
                gender = "male",
                street = "123 Main Street",
                city = "New York",
                state = "NY",
                country = "USA",
                postcode = "10001"
            )
        )
    }

    private fun ContactEntity.toContactDomain(): ContactDomain {
        return ContactDomain(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phone = this.phone,
            picture = this.picture,
            gender = this.gender,
            location = LocationDomain(
                street = this.street,
                city = this.city,
                state = this.state,
                country = this.country,
                postcode = this.postcode
            )
        )
    }
}
