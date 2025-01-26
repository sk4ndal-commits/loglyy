package org.example.project.data

import java.time.LocalDateTime

/**
 * Encapsulates filtering criteria for processing and displaying log data.
 *
 * @property filterText A text-based filter that specifies a keyword or phrase to match log entries. Set to null to disable this filter.
 * @property ignoreCase Indicates whether the `filterText` matching should ignore case sensitivity. Defaults to true.
 * @property startDateTime The lower bound of the datetime range filter. Entries occurring before this datetime will be excluded. Set to null to disable this filter.
 * @property endDateTime The upper bound of the datetime range filter. Entries occurring after this datetime will be excluded. Set to null to disable this filter.
 * @property loglevel Specifies a minimum log level for filtering. Only entries with this level or higher will pass the filter. Set to null to disable this filter.
 */
data class FilterSettings(
    val filterText: String? = null,
    val ignoreCase: Boolean = true,
    val startDateTime: LocalDateTime? = null,
    val endDateTime: LocalDateTime? = null,
    val loglevel: LogLevel? = null,
)