package com.helloumi.domain.model.result

import com.helloumi.domain.model.ContactDomain

sealed class ContactResult {

    data object Loading : ContactResult()

    data class Success(
        val contacts: List<ContactDomain>
    ) : ContactResult()

    data class FallbackFromError(
        val localContacts: List<ContactDomain>
    ) : ContactResult()
}
