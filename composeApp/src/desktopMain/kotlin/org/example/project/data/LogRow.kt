package org.example.project.data

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Represents a single row in the log data, encapsulating details about a specific log event.
 *
 * @property timeStamp The timestamp representing when the log event occurred. Can be null if not available.
 * @property level The logging level associated with the event (e.g., TRACE, DEBUG, INFO, WARN, ERROR). Can be null if unspecified.
 * @property source Indicates the source or origin of the log event (e.g., module name, class name). Can be null if not provided.
 * @property message The primary message of the log entry that describes the event. Can be null if not available.
 * @property detailMessage Additional details or context for the log event, often providing more in-depth information. Can be null if absent.
 */
data class LogRow(
    val timeStamp: LocalDateTime? = null,
    val level: LogLevel? = null,
    val source: String? = null,
    val message: String? = null,
    val detailMessage: String? = null
)

/**
 * Parses a batch of log data represented as a list of string lists and converts it into a structured list of LogRow objects.
 *
 * @param batch A list of lists where each inner list represents a row of log data. Each row contains string values representing
 * the fields of a LogRow: timestamp, log level, source, message, and detail message, in respective order.
 * @return A list of LogRow objects constructed from the input batch. If any field in a row cannot be parsed or is missing, the corresponding LogRow field will be null.
 */
fun parseBatch(batch: List<List<String>>): List<LogRow> {
    val twentyFourHourFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val twelveFourHourFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a")

    return batch.map { row ->
        LogRow(
            timeStamp = row.getOrNull(0)?.let {
                try {
                    LocalDateTime.parse(it)
                } catch (_: DateTimeParseException) {
                    try
                    {
                        LocalDateTime.parse(it, twentyFourHourFormat)
                    } catch (_: DateTimeParseException) {
                        try
                        {
                            LocalDateTime.parse(it, twelveFourHourFormat)
                        } catch (_: DateTimeParseException) {
                            null
                        }
                    }
                }
            },
            level = row.getOrNull(1)?.let {
                try {
                    LogLevel.valueOf(it.uppercase())
                } catch (_: IllegalArgumentException) {
                    null
                }
            },
            source = row.getOrNull(2),
            message = row.getOrNull(3),
            detailMessage = row.getOrNull(4)
        )
    }
}
