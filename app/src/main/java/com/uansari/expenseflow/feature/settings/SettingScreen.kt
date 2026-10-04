package com.uansari.expenseflow.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.uansari.expenseflow.feature.settings.components.SettingsItem
import com.uansari.expenseflow.feature.settings.components.SettingsSection
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsScreen(
    onNavigateToCategories: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is SettingsEvent.NavigateToCategories -> {
                    onNavigateToCategories()
                }

                is SettingsEvent.NavigateToAccounts -> {
                    onNavigateToAccounts()
                }

                is SettingsEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        SettingsContent(
            onCategoriesClick = viewModel::onCategoriesClick,
            onAccountsClick = viewModel::onAccountsClick,
            onExportDataClick = viewModel::onExportDataClick,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun SettingsContent(
    onCategoriesClick: () -> Unit,
    onAccountsClick: () -> Unit,
    onExportDataClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ════════════════════════════════════════════════════════════
        // Management Section
        // ════════════════════════════════════════════════════════════

        SettingsSection(title = "MANAGEMENT") {
            SettingsItem(
                icon = "🏷️",
                title = "Categories",
                subtitle = "Manage income and expense categories",
                onClick = onCategoriesClick
            )

            SettingsItem(
                icon = "💳",
                title = "Accounts",
                subtitle = "Manage your accounts",
                onClick = onAccountsClick
            )
        }

        // ════════════════════════════════════════════════════════════
        // Data Section
        // ════════════════════════════════════════════════════════════

        SettingsSection(title = "DATA") {
            SettingsItem(
                icon = "📤",
                title = "Export Data",
                subtitle = "Export transactions to CSV",
                onClick = onExportDataClick
            )
        }

        // ════════════════════════════════════════════════════════════
        // About Section
        // ════════════════════════════════════════════════════════════

        SettingsSection(title = "ABOUT") {
            SettingsItem(
                icon = "ℹ️", title = "App Version", subtitle = "1.0.0", showArrow = false
            )

            SettingsItem(
                icon = "👨‍💻",
                title = "Harsh Damani",
                subtitle = "Built with ❤️ using Kotlin & Jetpack Compose",
                showArrow = false,
                onClick = {
                    uriHandler.openUri("https://www.linkedin.com/in/harsh-damani-90b6913b3/")
                })
        }
    }
}