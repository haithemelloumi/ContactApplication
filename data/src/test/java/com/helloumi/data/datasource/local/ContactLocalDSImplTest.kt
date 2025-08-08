package com.helloumi.data.datasource.local

import com.helloumi.data.datasource.local.dao.ContactDao
import com.helloumi.data.datasource.local.entity.ContactEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for ContactLocalDSImpl.
 */
class ContactLocalDSImplTest {

    private lateinit var contactLocalDSImpl: ContactLocalDSImpl
    private lateinit var contactDao: ContactDao

    @Before
    fun setUp() {
        contactDao = mockk()
        contactLocalDSImpl = ContactLocalDSImpl(contactDao)
    }

    @Test
    fun `WHEN getAllContacts is called THEN should return contacts from dao`() = runTest {
        // GIVEN
        val mockContacts = createMockContactEntities()
        coEvery { contactDao.getAllContacts() } returns flowOf(mockContacts)

        // WHEN
        val result = contactLocalDSImpl.getAllContacts()

        // THEN
        val actualContacts = result.first()
        assertEquals(mockContacts.size, actualContacts.size)
        assertEquals(mockContacts[0].id, actualContacts[0].id)
        assertEquals(mockContacts[0].firstName, actualContacts[0].firstName)
    }

    @Test
    fun `WHEN insertContacts is called THEN verify dao call`() = runTest {
        // GIVEN
        val contactsToInsert = createMockContactEntities()
        coEvery { contactDao.insertContacts(contactsToInsert) } returns Unit

        // WHEN
        contactLocalDSImpl.insertContacts(contactsToInsert)

        // THEN
        coVerify { contactDao.insertContacts(contactsToInsert) }
    }

    @Test
    fun `WHEN deleteAllContacts is called THEN should delegate to dao`() = runTest {
        // GIVEN
        coEvery { contactDao.deleteAllContacts() } returns Unit

        // WHEN
        contactLocalDSImpl.deleteAllContacts()

        // THEN
        coVerify { contactDao.deleteAllContacts() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    // Helper methods
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
} 