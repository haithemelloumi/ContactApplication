package com.helloumi.ui.feature.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.helloumi.domain.model.result.ContactResult
import com.helloumi.domain.usecases.GetContactsUseCase
import com.helloumi.domain.usecases.GetLocalContactsUseCase
import com.helloumi.ui.utils.dispatchers.DispatcherProvider
import com.helloumi.ui.utils.network.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.first

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getContactsUseCase: GetContactsUseCase,
    private val getLocalContactsUseCase: GetLocalContactsUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsScreenState())
    val uiState: StateFlow<ContactsScreenState> = _uiState.asStateFlow()

    private val _effect = Channel<ContactsEffect>()
    val effect: Flow<ContactsEffect> = _effect.receiveAsFlow()

    private val currentPage get() = _uiState.value.currentPage

    fun observeInternetStatus() {
        viewModelScope.launch(dispatcherProvider.io) {
            networkMonitor.isOnline.distinctUntilChanged().collectLatest { isOnline ->
                val wasOnline = _uiState.value.isInternetAvailable
                _uiState.update { it.copy(isInternetAvailable = isOnline) }
                if (isOnline) {
                    loadContacts()
                    if (!wasOnline) {
                        // Reconnected after a period of offline time
                        _effect.send(ContactsEffect.ShowReconnectedMessage)
                    }
                } else {
                    loadLocalContacts()
                    if (wasOnline) {
                        // Disconnected after a period of online time
                        _effect.send(ContactsEffect.ShowDisconnectedMessage)
                    }
                }
            }
        }
    }

    fun processIntent(intent: ContactsIntent) {
        viewModelScope.launch(dispatcherProvider.io) {
            when (intent) {
                is ContactsIntent.LoadInitialData -> {
                    observeInternetStatus()
                }

                is ContactsIntent.ContactTapped -> {
                    _effect.send(
                        ContactsEffect.NavigateToContactDetails(
                            intent.contact
                        )
                    )
                }

                is ContactsIntent.LoadMoreContacts -> {
                    if (_uiState.value.isInternetAvailable && !_uiState.value.isLoadingContacts) {
                        loadMoreContacts()
                    }
                    // In offline mode, we don't load more contacts since we already have all contacts
                }
            }
        }
    }

    // Called for first page
    suspend fun loadContacts() {
        // Reset contact list and page when loading contacts from API
        _uiState.update { it.copy(isLoadingContacts = true, contacts = emptyList(), currentPage = INITIAL_PAGE) }

        getContactsUseCase(INITIAL_PAGE, RESULT_SIZE).collectLatest { result ->
            when (result) {
                is ContactResult.Success -> {
                    val newContacts = result.contacts
                    _uiState.update {
                        it.copy(
                            isLoadingContacts = false,
                            contacts = newContacts,
                            currentPage = INITIAL_PAGE
                        )
                    }
                }

                is ContactResult.FallbackFromError -> {
                    val newContacts = result.localContacts
                    _uiState.update {
                        it.copy(
                            isLoadingContacts = false,
                            contacts = newContacts,
                            currentPage = INITIAL_PAGE
                        )
                    }
                }

                is ContactResult.Loading -> {
                    _uiState.update { it.copy(isLoadingContacts = true) }
                }
            }
        }
    }

    // Called when scrolling
    suspend fun loadMoreContacts() {
        _uiState.update { it.copy(isLoadingMore = true) }

        val nextPage = currentPage + 1

        getContactsUseCase(nextPage, RESULT_SIZE).collectLatest { result ->
            when (result) {

                is ContactResult.Success -> {
                    val newContacts = result.contacts
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            contacts = it.contacts + newContacts,
                            currentPage = nextPage
                        )
                    }
                }

                is ContactResult.FallbackFromError -> {
                    val newContacts = result.localContacts
                    _uiState.update {
                        it.copy(
                            isLoadingMore = false,
                            contacts = it.contacts + newContacts,
                            currentPage = INITIAL_PAGE
                        )
                    }
                }

                is ContactResult.Loading -> {
                    _uiState.update { it.copy(isLoadingMore = true) }
                }
            }
        }
    }

    private fun loadLocalContacts() {
        viewModelScope.launch(dispatcherProvider.io) {
            try {
                val localContacts = getLocalContactsUseCase().first()
                // Only update if we're still offline
                if (!_uiState.value.isInternetAvailable) {
                    _uiState.update {
                        it.copy(
                            isLoadingContacts = false,
                            contacts = localContacts,
                            currentPage = INITIAL_PAGE
                        )
                    }
                }
            } catch (e: Exception) {
                // Handle any errors
                println("Error loading local contacts: ${e.message}")
            }
        }
    }

    companion object {
        const val INITIAL_PAGE = 1
        const val RESULT_SIZE = 20
        const val PREFETCH_DISTANCE = 5
    }
}
