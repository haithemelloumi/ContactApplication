package com.helloumi.data.datasource.remote

import com.helloumi.data.datasource.remote.api.RandomUserAPI
import com.helloumi.data.datasource.remote.response.RandomUserInfo
import com.helloumi.data.datasource.remote.response.RandomUserLocation
import com.helloumi.data.datasource.remote.response.RandomUserName
import com.helloumi.data.datasource.remote.response.RandomUserPicture
import com.helloumi.data.datasource.remote.response.RandomUserResponse
import com.helloumi.data.datasource.remote.response.RandomUserResult
import com.helloumi.data.datasource.remote.response.RandomUserStreet
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for RandomUserRemoteDSImpl.
 */
class RandomUserRemoteDSImplTest {

    private lateinit var randomUserRemoteDSImpl: RandomUserRemoteDSImpl
    private lateinit var randomUserAPI: RandomUserAPI

    @Before
    fun setUp() {
        randomUserAPI = mockk()
        randomUserRemoteDSImpl = RandomUserRemoteDSImpl(randomUserAPI)
    }

    @Test
    fun `WHEN getContacts is called THEN should delegate to API`() = runTest {
        // GIVEN
        val results = 20
        val page = 1
        val mockResponse = createMockRandomUserResponse()
        coEvery { randomUserAPI.getContacts(results = results, page = page) } returns mockResponse

        // WHEN
        val result = randomUserRemoteDSImpl.getContacts(results, page)

        // THEN
        assertEquals(mockResponse, result)
        coVerify { randomUserAPI.getContacts(results = results, page = page) }
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
} 