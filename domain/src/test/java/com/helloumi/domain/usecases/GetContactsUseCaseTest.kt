package com.helloumi.domain.usecases

import com.helloumi.domain.model.ContactDomain
import com.helloumi.domain.model.LocationDomain
import com.helloumi.domain.model.result.ContactResult
import com.helloumi.domain.repository.ContactRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for GetContactsUseCase.
 */
class GetContactsUseCaseTest {

    private lateinit var getContactsUseCase: GetContactsUseCase
    private lateinit var contactRepository: ContactRepository

    @Before
    fun setUp() {
        contactRepository = mockk()
        getContactsUseCase = GetContactsUseCase(contactRepository)
    }

    @Test
    fun `WHEN invoke is called THEN should assert values`() = runTest {
        // GIVEN
        val page = 1
        val results = 20
        val mockContacts = createMockContactDomains()
        val mockContactResult = ContactResult.Success(mockContacts)

        coEvery { contactRepository.getContacts(page, results) } returns flowOf(mockContactResult)

        // WHEN
        val result = getContactsUseCase(page, results)

        // THEN
        val actualResult = result.first()
        assertTrue(actualResult is ContactResult.Success)
        val successResult = actualResult as ContactResult.Success
        assertEquals(mockContacts.size, successResult.contacts.size)
        assertEquals(mockContacts[0].firstName, successResult.contacts[0].firstName)
        assertEquals(mockContacts[0].lastName, successResult.contacts[0].lastName)
        assertEquals(mockContacts[0].email, successResult.contacts[0].email)
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
            ),
            ContactDomain(
                id = "2",
                firstName = "Jane",
                lastName = "Smith",
                email = "jane.smith@example.com",
                phone = "+1987654321",
                picture = "https://randomuser.me/api/portraits/women/1.jpg",
                gender = "female",
                location = LocationDomain(
                    street = "456 Oak Avenue",
                    city = "Los Angeles",
                    state = "CA",
                    country = "USA",
                    postcode = "90210"
                )
            )
        )
    }
} 