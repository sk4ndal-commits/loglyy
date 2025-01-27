package org.example.project

import App
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

// Custom ViewModelStoreOwner for Desktop Application
class DesktopViewModelStoreOwner : ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
}

fun main() = application {
    val viewModelStoreOwner = DesktopViewModelStoreOwner()

    Window(
        onCloseRequest = ::exitApplication,
        title = "loglyy",
    ) {
        // Provide the ViewModelStoreOwner to the CompositionLocal
        CompositionLocalProvider(
            LocalViewModelStoreOwner provides viewModelStoreOwner
        ) {
            App()
        }
    }
}