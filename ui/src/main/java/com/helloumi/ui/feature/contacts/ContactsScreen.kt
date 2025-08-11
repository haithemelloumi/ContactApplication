package com.helloumi.ui.feature.contacts

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.helloumi.domain.model.ContactDomain
import com.helloumi.domain.model.LocationDomain
import com.helloumi.ui.R
import com.helloumi.ui.feature.contacts.ContactsViewModel.Companion.PREFETCH_DISTANCE
import com.helloumi.ui.theme.Dimens
import com.helloumi.ui.theme.PURPLE_40
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier,
    viewModel: ContactsViewModel = hiltViewModel(),
    navigateToContactDetails: (contact: ContactDomain) -> Unit
) {
    // Collect State
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    
    // Track if initial data has been loaded to prevent reloading on rotation
    val hasInitialDataLoaded = rememberSaveable { mutableStateOf(false) }

    val reconnectedMessage = stringResource(R.string.reconnected_message)
    val disconnectedMessage = stringResource(R.string.disconnected_message)

    // Manage effects (navigation and notifications)
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ContactsEffect.NavigateToContactDetails -> {
                    navigateToContactDetails(effect.contact)
                }

                is ContactsEffect.ShowReconnectedMessage -> {
                    snackBarHostState.showSnackbar(
                        message = reconnectedMessage,
                        duration = SnackbarDuration.Long
                    )
                }

                is ContactsEffect.ShowDisconnectedMessage -> {
                    snackBarHostState.showSnackbar(
                        message = disconnectedMessage,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

    // Send initial intent to load data only once
    LaunchedEffect(Unit) {
        if (!hasInitialDataLoaded.value) {
            viewModel.processIntent(ContactsIntent.LoadInitialData)
            hasInitialDataLoaded.value = true
        }
    }

    // Manage intents (contactTapped and loadMoreContacts)
    Box(modifier = modifier.fillMaxSize()) {
        ContactsContent(
            modifier = Modifier.fillMaxSize(),
            uiState = uiState,
            onClickContact = { contact ->
                viewModel.processIntent(ContactsIntent.ContactTapped(contact))
            },
            onLoadMore = {
                viewModel.processIntent(ContactsIntent.LoadMoreContacts)
            }
        )

        // SnackBar for notifications
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
fun ContactsContent(
    modifier: Modifier,
    uiState: ContactsScreenState,
    onClickContact: (ContactDomain) -> Unit,
    onLoadMore: () -> Unit
) {
    val scrollState = rememberLazyListState()
    val systemUiController = rememberSystemUiController()
    val isDarkTheme = isSystemInDarkTheme()

    // LoadMore when reaching the end of the list
    LaunchedEffect(scrollState) {
        snapshotFlow {
            val lastVisibleItem = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = scrollState.layoutInfo.totalItemsCount
            lastVisibleItem?.index to totalItemsCount
        }.collect { (lastVisibleIndex, totalItems) ->
            // Only load more if we are online and we are in the last prefetch_distance visible elements
            if (uiState.isInternetAvailable &&
                !uiState.isLoadingMore &&
                lastVisibleIndex != null &&
                totalItems > 0 &&
                lastVisibleIndex >= totalItems - PREFETCH_DISTANCE
            ) {
                onLoadMore()
            }
        }
    }

    systemUiController.setStatusBarColor(
        color = PURPLE_40,
        darkIcons = if (isDarkTheme) false else scrollState.firstVisibleItemScrollOffset != 0
    )

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        // Display Loading State
        if (uiState.isLoadingContacts && uiState.contacts.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = Dimens.STACK_MD, vertical = Dimens.STACK_SM)
            ) {
                items(
                    uiState.contacts,
                    key = { contact -> contact.id }
                ) { contact ->
                    ContactItem(
                        contact = contact,
                        onClickContact = { onClickContact(contact) }
                    )
                    Spacer(modifier = Modifier.height(Dimens.STACK_XS))
                }

                // Load more indicator
                if (uiState.isLoadingMore) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Dimens.STACK_MD),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            // Display message when no contacts are available
            if (uiState.contacts.isEmpty() && !uiState.isLoadingContacts) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.no_contacts))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContactsPreview() {
    val sampleState = ContactsScreenState(
        isLoadingContacts = false,
        contacts = listOf(
            ContactDomain(
                id = "1",
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@example.com",
                phone = "+1234567890",
                picture = "",
                gender = "male",
                location = LocationDomain(
                    street = "123 Main St",
                    city = "New York",
                    state = "NY",
                    country = "USA",
                    postcode = "10001"
                )
            )
        )
    )
    ContactsContent(
        modifier = Modifier,
        uiState = sampleState,
        onClickContact = { _ -> },
        onLoadMore = {}
    )
}
