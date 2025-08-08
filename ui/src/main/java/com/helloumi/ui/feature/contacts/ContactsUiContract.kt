package com.helloumi.ui.feature.contacts

import com.helloumi.domain.model.ContactDomain

// ---------- UI STATE ----------
data class ContactsScreenState(
    val isLoadingContacts: Boolean = true,
    val contacts: List<ContactDomain> = emptyList(),
    val currentPage: Int = 1,
    val isLoadingMore: Boolean = false,
    val isInternetAvailable: Boolean = true
)

// ---------- INTENTS (UI events -> ViewModel) ----------
sealed interface ContactsIntent {
    data object LoadInitialData : ContactsIntent
    data class ContactTapped(val contact: ContactDomain) : ContactsIntent
    data object LoadMoreContacts : ContactsIntent
}

// ---------- EFFECTS (ViewModel -> UI one-time events) ----------
sealed class ContactsEffect {
    data class NavigateToContactDetails(val contact: ContactDomain) : ContactsEffect()
    data object ShowReconnectedMessage : ContactsEffect()
    data object ShowDisconnectedMessage : ContactsEffect()
}
