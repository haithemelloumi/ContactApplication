package com.helloumi.ui.feature.contacts

import com.helloumi.domain.model.ContactDomain
import com.helloumi.domain.model.LocationDomain
import com.helloumi.domain.model.result.ContactResult
import com.helloumi.domain.usecases.GetContactsUseCase
import com.helloumi.domain.usecases.GetLocalContactsUseCase
import com.helloumi.ui.feature.contacts.ContactsViewModel.Companion.INITIAL_PAGE
import com.helloumi.ui.feature.contacts.ContactsViewModel.Companion.RESULT_SIZE
import com.helloumi.ui.utils.dispatchers.DispatcherProvider
import com.helloumi.ui.utils.network.NetworkMonitor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContactsViewModelTest {

    private lateinit var contactsViewModel: ContactsViewModel
    private lateinit var getContactsUseCase: GetContactsUseCase
    private lateinit var getLocalContactsUseCase: GetLocalContactsUseCase
    private lateinit var dispatcherProvider: DispatcherProvider
    private lateinit var networkMonitor: NetworkMonitor

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        getContactsUseCase = mockk()
        getLocalContactsUseCase = mockk()
        dispatcherProvider = mockk()
        networkMonitor = mockk()

        coEvery { dispatcherProvider.io } returns testDispatcher
        coEvery { dispatcherProvider.main } returns testDispatcher

        contactsViewModel = ContactsViewModel(
            dispatcherProvider = dispatcherProvider,
            getContactsUseCase = getContactsUseCase,
            getLocalContactsUseCase = getLocalContactsUseCase,
            networkMonitor = networkMonitor
        )

        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN loadContacts is called with success THEN should update state with contacts`() = runTest {
        // GIVEN
        val mockContacts = createMockContactDomains()
        val mockContactResult = ContactResult.Success(mockContacts)

        coEvery { getContactsUseCase(INITIAL_PAGE, RESULT_SIZE) } returns flowOf(mockContactResult)

        // WHEN
        contactsViewModel.loadContacts()

        // THEN
        val currentState = contactsViewModel.uiState.value
        assertFalse(currentState.isLoadingContacts)
        assertEquals(mockContacts.size, currentState.contacts.size)
        assertEquals(mockContacts[0].firstName, currentState.contacts[0].firstName)
        coVerify { getContactsUseCase(INITIAL_PAGE, RESULT_SIZE) }
    }

    @Test
    fun `WHEN loadMoreContacts is called with fallback THEN should append local contacts`() = runTest {
        // GIVEN
        val newContacts = listOf(createMockContactDomain("2", "Jane", "Smith"))
        val mockContactResult = ContactResult.FallbackFromError(newContacts)

        coEvery { getContactsUseCase(2, RESULT_SIZE) } returns flowOf(mockContactResult)

        // WHEN
        contactsViewModel.loadMoreContacts()

        // THEN
        val currentState = contactsViewModel.uiState.value
        assertFalse(currentState.isLoadingMore)
        assertEquals(newContacts.size, currentState.contacts.size)
        assertEquals(1, currentState.currentPage) // Should reset to initial page on fallback
        coVerify { getContactsUseCase(2, RESULT_SIZE) }
    }

    // Helper methods
    private fun createMockContactDomains(): List<ContactDomain> {
        return listOf(
            createMockContactDomain("1", "John", "Doe"),
            createMockContactDomain("2", "Jane", "Smith")
        )
    }

    private fun createMockContactDomain(
        id: String = "1",
        firstName: String = "John",
        lastName: String = "Doe"
    ): ContactDomain {
        return ContactDomain(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = "$firstName.$lastName@example.com",
            phone = "+1234567890",
            picture = "https://randomuser.me/api/portraits/men/$id.jpg",
            gender = "male",
            location = LocationDomain(
                street = "123 Main Street",
                city = "New York",
                state = "NY",
                country = "USA",
                postcode = "10001"
            )
        )
    }
} 