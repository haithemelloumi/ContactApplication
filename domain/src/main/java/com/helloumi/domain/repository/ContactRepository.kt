package com.helloumi.domain.repository

import com.helloumi.domain.model.ContactDomain
import com.helloumi.domain.model.result.ContactResult
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    /**
     * Requests contacts from the RandomUser API.
     *
     * @param page the page number for pagination.
     * @param results the number of results per page.
     *
     * @return a ContactResult Flow
     */
    fun getContacts(page: Int, results: Int): Flow<ContactResult>

    /**
     * Gets contacts from local database.
     *
     * @return a list of ContactDomain Flow
     */
    fun getLocalContacts(): Flow<List<ContactDomain>>
}
