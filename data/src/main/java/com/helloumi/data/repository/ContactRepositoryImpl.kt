package com.helloumi.data.repository

import com.helloumi.data.datasource.local.ContactLocalDS
import com.helloumi.data.datasource.local.entity.ContactEntity
import com.helloumi.data.datasource.remote.RandomUserRemoteDS
import com.helloumi.data.mapper.toContactDomain
import com.helloumi.domain.model.ContactDomain
import com.helloumi.domain.model.LocationDomain
import com.helloumi.domain.model.result.ContactResult
import com.helloumi.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val randomUserRemoteDS: RandomUserRemoteDS,
    private val localDS: ContactLocalDS
) : ContactRepository {

    override fun getContacts(page: Int, results: Int) = flow {
        emit(ContactResult.Loading)
        try {
            val response = randomUserRemoteDS.getContacts(page = page, results = results)
            val contacts = response.results.map { it.toContactDomain() }
            saveContactsToLocal(contacts)
            emit(ContactResult.Success(contacts))
        } catch (_: IOException) {
            fallbackToLocal()
        } catch (_: Exception) {
            fallbackToLocal()
        }
    }

    override fun getLocalContacts(): Flow<List<ContactDomain>> {
        return localDS.getAllContacts().map { entities ->
            entities.map { entity ->
                ContactDomain(
                    id = entity.id,
                    firstName = entity.firstName,
                    lastName = entity.lastName,
                    email = entity.email,
                    phone = entity.phone,
                    picture = entity.picture,
                    gender = entity.gender,
                    location = LocationDomain(
                        street = entity.street,
                        city = entity.city,
                        state = entity.state,
                        country = entity.country,
                        postcode = entity.postcode
                    )
                )
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Internal
    ///////////////////////////////////////////////////////////////////////////

    private suspend fun saveContactsToLocal(contacts: List<ContactDomain>) {
        saveContacts(contacts)
    }

    private suspend fun FlowCollector<ContactResult>.fallbackToLocal() {
        emitAll(getLocalContacts().map { ContactResult.FallbackFromError(it) })
    }

    private suspend fun saveContacts(contacts: List<ContactDomain>) {
        val entities = contacts.map { contact ->
            ContactEntity(
                id = contact.id,
                firstName = contact.firstName,
                lastName = contact.lastName,
                email = contact.email,
                phone = contact.phone,
                picture = contact.picture,
                gender = contact.gender,
                street = contact.location.street,
                city = contact.location.city,
                state = contact.location.state,
                country = contact.location.country,
                postcode = contact.location.postcode
            )
        }
        localDS.insertContacts(entities)
    }
}
