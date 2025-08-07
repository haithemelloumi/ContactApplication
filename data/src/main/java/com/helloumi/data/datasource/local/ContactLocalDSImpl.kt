package com.helloumi.data.datasource.local

import com.helloumi.data.datasource.local.dao.ContactDao
import com.helloumi.data.datasource.local.entity.ContactEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactLocalDSImpl @Inject constructor(
    private val dao: ContactDao
) : ContactLocalDS {
    override fun getAllContacts(): Flow<List<ContactEntity>> = dao.getAllContacts()
    override suspend fun insertContacts(contacts: List<ContactEntity>) = dao.insertContacts(contacts)
    override suspend fun deleteAllContacts() = dao.deleteAllContacts()
}
