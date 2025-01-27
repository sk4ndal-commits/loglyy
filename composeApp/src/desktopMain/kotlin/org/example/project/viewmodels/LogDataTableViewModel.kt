package org.example.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.project.data.FilterSettings
import org.example.project.data.LogRow
import org.example.project.providers.IReaderProvider
import java.time.LocalDateTime

class LogDataTableViewModel(
    private val readerProvider: IReaderProvider,
    private val batchSize: Int = 100
) : ViewModel()
{

    private val _rows = MutableStateFlow<List<LogRow>>(emptyList())
    val rows: StateFlow<List<LogRow>> = _rows

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _endOfFile = MutableStateFlow(false)
    val endOfFile: StateFlow<Boolean> = _endOfFile

    private val _filter = MutableStateFlow<String?>(null)
    val filter: StateFlow<String?> = _filter

    private var filePath: String? = null

    private var cursorPosition: Int = 0


    fun setFilePath(filePath: String)
    {
        this.filePath = filePath
        this.loadInitialRows(FilterSettings())
    }

    private suspend fun readRows(
        filterSettings: FilterSettings,
        initialize: Boolean
    )
    {
        filePath?.let { path ->
            val reader = readerProvider.getReader(path)

            // If initialize is true, reset cursor and rows
            if (initialize)
            {
                _rows.value = emptyList() // Clear existing rows
                cursorPosition = 0        // Reset cursor to beginning of the file
                _endOfFile.value = false
            }

            val totalRows = _rows.value.toMutableList() // Use existing rows if not initializing
            var linesSkipped = 0 // Track how many rows are skipped in loadMoreRows

            reader.process(path, batchSize).collect { batch ->
                if (initialize)
                {
                    // For `loadInitialRows`, we just start reading right away
                    if (batch.isEmpty())
                    {
                        _endOfFile.value = true
                        return@collect
                    }
                } else
                {
                    // For `loadMoreRows`, skip already-read rows
                    if (linesSkipped < cursorPosition)
                    {
                        linesSkipped += batch.size
                        return@collect
                    }
                }

                // If we receive an empty batch, we've reached EOF
                if (batch.isEmpty())
                {
                    _endOfFile.value = true
                    return@collect
                }

                // Apply filters to rows in the current batch
                val filtered = applyFilters(batch, filterSettings)

                // Append new rows to the collection
                totalRows.addAll(filtered)
                cursorPosition += batch.size // Update the cursor

                // Update data in the state flow
                _rows.value = totalRows

                // If fewer rows than `batchSize` were returned, EOF might have been reached
                if (batch.size < batchSize)
                {
                    _endOfFile.value = true
                    return@collect
                }
            }

            // End the loading state after all batches are processed
            _isLoading.value = false
        }
    }

    // Load the initial rows (wrapper around `readRows`)
    private fun loadInitialRows(filterSettings: FilterSettings)
    {
        if (filePath == null) return
        _isLoading.value = true
        viewModelScope.launch {
            readRows(filterSettings, initialize = true) // Reset cursor and clear rows
        }
    }

    // Load additional rows lazily when scrolling (wrapper around `readRows`)
    fun loadMoreRows(filterSettings: FilterSettings)
    {
        if (_isLoading.value || _endOfFile.value || filePath == null) return
        _isLoading.value = true
        viewModelScope.launch {
            readRows(filterSettings, initialize = false) // Continue from current cursor
        }
    }


    private fun applyFilters(rows: List<LogRow>, filterSettings: FilterSettings): List<LogRow>
    {
        return rows.filter { row ->
            // Text filtering: Match if any non-null field in LogRow contains the filterText
            val matchesText = filterSettings.filterText?.let { text ->
                listOfNotNull(
                    row.timeStamp?.toString(),
                    row.level?.toString(),
                    row.source,
                    row.message,
                    row.detailMessage
                ).any { it.contains(text, ignoreCase = filterSettings.ignoreCase) }
            } ?: true

            // Date range filtering: Check timeStamp is within range only if provided
            val matchesDateRange = filterSettings.startDateTime?.let { start ->
                row.timeStamp?.let { timeStamp ->
                    timeStamp >= start && (filterSettings.endDateTime?.let { end -> timeStamp <= end } ?: true)
                } ?: true
            } ?: true

            // Log level filtering: Match log level only if provided
            val matchesLogLevel = filterSettings.loglevel?.let { level ->
                row.level?.equals(level) ?: true
            } ?: true

            // Combine all filters
            matchesText && matchesDateRange && matchesLogLevel
        }
    }
}