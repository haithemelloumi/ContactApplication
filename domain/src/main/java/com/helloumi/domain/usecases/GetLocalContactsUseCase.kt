package com.helloumi.domain.usecases

import com.helloumi.domain.repository.ContactRepository
import javax.inject.Inject

/**
 * Gets contacts from database.
 */
class GetLocalContactsUseCase @Inject constructor(private val contactRepository: ContactRepository) {

    /**
     * Invokes use case.
     * @return the contact result Flow.
     */
    operator fun invoke() = contactRepository.getLocalContacts()
}
