package com.helloumi.domain.usecases

import com.helloumi.domain.model.ContactDomain
import com.helloumi.domain.model.LocationDomain
import com.helloumi.domain.repository.ContactRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for GetLocalContactsUseCase.
 */
class GetLocalContactsUseCaseTest {

    private lateinit var getLocalContactsUseCase: GetLocalContactsUseCase
    private lateinit var contactRepository: ContactRepository

    @Before
    fun setUp() {
        contactRepository = mockk()
        getLocalContactsUseCase = GetLocalContactsUseCase(contactRepository)
    }

    @Test
    fun `WHEN invoke is called THEN should verify repository and assert values`() = runTest {
        // GIVEN
        val mockContacts = createMockContactDomains()
        coEvery { contactRepository.getLocalContacts() } returns flowOf(mockContacts)

        // WHEN
        val result = getLocalContactsUseCase()

        // THEN
        val actualContacts = result.first()
        assertEquals(mockContacts.size, actualContacts.size)
        assertEquals(mockContacts[0].id, actualContacts[0].id)
        assertEquals(mockContacts[0].firstName, actualContacts[0].firstName)
    }

    // Helper methods
    private fun createMockContactDomains(): List<ContactDomain> {
        return listOf(
            ContactDomain(
                id = "1",
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@example.com",
                phone = "+1234567890",
                picture = "https://randomuser.me/api/portraits/men/1.jpg",
                gender = "male",
                location = LocationDomain(
                    street = "123 Main Street",
                    city = "New York",
                    state = "NY",
                    country = "USA",
                    postcode = "10001"
                )
            )
        )
    }
} 