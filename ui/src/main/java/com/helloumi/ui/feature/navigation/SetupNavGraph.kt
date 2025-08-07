package com.helloumi.ui.feature.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.helloumi.domain.model.ContactDomain
import com.helloumi.ui.feature.contactdetails.ContactDetailsScreen
import com.helloumi.ui.feature.contacts.ContactsScreen
import com.helloumi.ui.theme.PURPLE_40
import com.helloumi.ui.R
import com.helloumi.ui.feature.main.MainActivity.Companion.CONTACT_KEY
import com.helloumi.ui.theme.Dimens
import com.helloumi.ui.theme.WHITE

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupNavGraph() {

    val title = mutableStateOf(stringResource(R.string.contact_title))
    val navController: NavHostController = rememberNavController()

    LaunchedEffect(navController.currentBackStackEntryFlow) {
        navController.currentBackStackEntryFlow.collect {
            title.value = it.destination.route ?: ""
        }
    }

    Scaffold(
        containerColor = WHITE,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = getTitleForRoute(title.value),
                        style = TextStyle(fontSize = Dimens.TEXT_SIZE_BIG),
                        color = WHITE
                    )
                },

                ///// ADDED TO RETURN BACK //////
                navigationIcon = {
                    if (title.value != Navigation.Contacts.destination) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                                contentDescription = ""
                            )
                        }
                    }
                },
                ///// ADDED TO RETURN BACK //////

                // Customize Colors here
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PURPLE_40,
                    titleContentColor = WHITE,
                    navigationIconContentColor = WHITE,
                    actionIconContentColor = WHITE
                ),
            )
        },
        content = { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = Navigation.Contacts.destination
            ) {
                composable(Navigation.Contacts.destination) {
                    ContactsScreen(
                        modifier = Modifier.padding(innerPadding),
                        navigateToContactDetails = { contact ->
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                CONTACT_KEY,
                                contact
                            )
                            navController.navigate(Navigation.Details.destination)
                        }
                    )
                }

                composable(Navigation.Details.destination) {
                    val contact =
                        navController.previousBackStackEntry?.savedStateHandle?.get<ContactDomain>(
                            CONTACT_KEY
                        )
                    if (contact != null) {
                        ContactDetailsScreen(
                            modifier = Modifier.padding(innerPadding),
                            contact = contact
                        )
                    }
                }
            }

        },
    )
}

@Composable
private fun getTitleForRoute(route: String) =
    when (route) {
        Navigation.Contacts.destination -> stringResource(R.string.contact_title)
        Navigation.Details.destination -> stringResource(R.string.contact_details)
        else -> ""
    }
