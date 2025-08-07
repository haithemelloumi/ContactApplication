package com.helloumi.domain.usecases

import com.helloumi.domain.repository.ContactRepository
import javax.inject.Inject

/**
 * Gets contacts from the API with pagination.
 */
class GetContactsUseCase @Inject constructor(private val contactRepository: ContactRepository) {

    /**
     * Executes use case.
     *
     * @param page the page number for pagination.
     * @param results the number of results per page.
     * @return the contact result Flow.
     */
    operator fun invoke(page: Int, results: Int) = contactRepository.getContacts(page, results)
}
