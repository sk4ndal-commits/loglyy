package org.example.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.project.data.FilterSettings
import org.example.project.data.LogRow
import org.example.project.providers.ReaderProvider
import java.time.LocalDateTime

class LogDataTableViewModel(
    private val readerProvider: ReaderProvider,
    private val batchSize: Int = 1000
) : ViewModel()
{

    private val _rows = MutableStateFlow<List<LogRow>>(emptyList())
    val rows: StateFlow<List<LogRow>> = _rows

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading;

    private val _endOfFile = MutableStateFlow(false)
    val endOfFile: StateFlow<Boolean> = _endOfFile;

    private val _filter = MutableStateFlow<String?>(null)
    val filter: StateFlow<String?> = _filter;

    private var filePath: String? = null


    fun setFilePath(filePath: String)
    {
        this.filePath = filePath;
        this.loadInitialRows(FilterSettings());
    }

    private fun loadInitialRows(filterSettings: FilterSettings) {
        this.filePath?.let { path ->
            val reader = readerProvider.getReader(path)
            _isLoading.value = true
            _endOfFile.value = false

            viewModelScope.launch {
                val filteredBatch = mutableListOf<LogRow>()

                reader.process(path, batchSize).collect { batch ->
                    if (batch.isEmpty()) {
                        _endOfFile.value = true
                    } else {
                        // Filter the batch
                        val filtered = applyFilters(batch, filterSettings)
                        filteredBatch.addAll(filtered)

                        // Keep reading until we have at least `batchSize` rows or EOF
                        if (filteredBatch.size >= batchSize || _endOfFile.value) {
                            _rows.value = filteredBatch.take(batchSize) // Take only up to batch size
                            _isLoading.value = false
                            return@collect
                        }
                    }
                }
                _isLoading.value = false
            }
        }
    }

    fun loadMoreRows(filterSettings: FilterSettings) {
        if (_isLoading.value || _endOfFile.value) return

        this.filePath?.let { path ->
            val reader = readerProvider.getReader(path)
            _isLoading.value = true

            viewModelScope.launch {
                val filteredBatch = _rows.value.toMutableList()

                reader.process(path, batchSize).collect { batch ->
                    if (batch.isEmpty()) {
                        _endOfFile.value = true
                    } else {
                        // Filter the batch and add to the accumulated filtered rows
                        val filtered = applyFilters(batch, filterSettings)
                        filteredBatch.addAll(filtered)

                        // Keep reading until filteredBatch reaches `batchSize` or EOF
                        if (filteredBatch.size >= _rows.value.size + batchSize || _endOfFile.value) {
                            _rows.value = filteredBatch.take(_rows.value.size + batchSize) // Ensure added rows match total batchSize
                            _isLoading.value = false
                            return@collect
                        }
                    }
                }
                _isLoading.value = false
            }
        }
    }

    private fun applyFilters(rows: List<LogRow>, filterSettings: FilterSettings): List<LogRow> {
        return rows.filter { row ->
            // Text filtering: Match if any non-null field in LogRow contains the filterText
            val matchesText = filterSettings.filterText?.let { text ->
                listOfNotNull(
                    row.timeStamp?.toString(),
                    row.level?.toString(),
                    row.source,
                    row.message,
                    row.detailMessage
                ).any { it.contains(text, ignoreCase = filterSettings.ignoreCase ?: true) }
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

    // Helper: Convert a string to LocalDateTime (if valid)
    private fun String.toLocalDateTimeOrNull(): LocalDateTime? {
        return try {
            LocalDateTime.parse(this) // Adjust the parsing format if needed
        } catch (e: Exception) {
            null
        }
    }
}