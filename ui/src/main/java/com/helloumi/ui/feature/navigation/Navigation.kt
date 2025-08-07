package com.helloumi.ui.feature.navigation

sealed class Navigation(val destination: String) {
    data object Contacts: Navigation("contacts")
    data object Details: Navigation("details")
}
