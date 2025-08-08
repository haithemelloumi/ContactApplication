package com.helloumi.data.datasource.local

import com.helloumi.data.datasource.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow


interface ContactLocalDS {

    /**
     * Requests list of ContactEntity from the database.
     *
     * @return a ContactEntity List Flow
     */
    fun getAllContacts(): Flow<List<ContactEntity>>

    /**
     * Inserts list of ContactEntity to the database.
     *
     * @param contacts the list of ContactEntity to insert.
     */
    suspend fun insertContacts(contacts: List<ContactEntity>)

    /**
     * Deletes all contacts from the database.
     *
     * @return a ContactEntity List Flow
     */
    suspend fun deleteAllContacts()
}
