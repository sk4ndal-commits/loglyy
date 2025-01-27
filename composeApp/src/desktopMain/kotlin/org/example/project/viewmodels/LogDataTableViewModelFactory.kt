package org.example.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import org.example.project.providers.ReaderProvider
import kotlin.reflect.KClass

class LogDataTableViewModelFactory(
    private val readerProvider: ReaderProvider,
    private val batchSize: Int = 1000
) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: KClass<T>, extras: CreationExtras): T
    {
        if (modelClass.java.isAssignableFrom(LogDataTableViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LogDataTableViewModel(readerProvider, batchSize) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}