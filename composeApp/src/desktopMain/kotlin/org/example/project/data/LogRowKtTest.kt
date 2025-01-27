package org.example.project.data

import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LogRowKtTest
{

    /**
     * Class: LogRow
     * Method: parseBatch
     * Description: Tests for the parseBatch function. This function converts a batch of log rows represented
     * as lists of strings into a list of LogRow objects, parsing their fields into the appropriate types.
     */

    @Test
    fun `parseBatch should correctly parse valid input data`()
    {
        val inputBatch = listOf(
            listOf("2023-10-01 12:34:56", "INFO", "Source1", "Message1", "DetailMessage1"),
            listOf("2023-10-02T15:20:30", "ERROR", "Source2", "Message2", "DetailMessage2")
        )

        val result = parseBatch(inputBatch)

        assertEquals(2, result.size)
        assertEquals(LocalDateTime.parse("2023-10-01 12:34:56", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), result[0].timeStamp)
        assertEquals(LogLevel.INFO, result[0].level)
        assertEquals("Source1", result[0].source)
        assertEquals("Message1", result[0].message)
        assertEquals("DetailMessage1", result[0].detailMessage)

        assertEquals(LocalDateTime.parse("2023-10-02T15:20:30"), result[1].timeStamp)
        assertEquals(LogLevel.ERROR, result[1].level)
        assertEquals("Source2", result[1].source)
        assertEquals("Message2", result[1].message)
        assertEquals("DetailMessage2", result[1].detailMessage)
    }

    @Test
    fun `parseBatch should handle invalid timestamp gracefully`()
    {
        val inputBatch = listOf(
            listOf("invalid-timestamp", "WARN", "Source1", "Message1", "DetailMessage1")
        )

        val result = parseBatch(inputBatch)

        assertEquals(1, result.size)
        assertNull(result[0].timeStamp)
        assertEquals(LogLevel.WARN, result[0].level)
        assertEquals("Source1", result[0].source)
        assertEquals("Message1", result[0].message)
        assertEquals("DetailMessage1", result[0].detailMessage)
    }

    @Test
    fun `parseBatch should handle invalid log level gracefully`()
    {
        val inputBatch = listOf(
            listOf("2023-10-01T12:34:56", "INVALID_LEVEL", "Source1", "Message1", "DetailMessage1")
        )

        val result = parseBatch(inputBatch)

        assertEquals(1, result.size)
        assertEquals(LocalDateTime.parse("2023-10-01T12:34:56"), result[0].timeStamp)
        assertNull(result[0].level)
        assertEquals("Source1", result[0].source)
        assertEquals("Message1", result[0].message)
        assertEquals("DetailMessage1", result[0].detailMessage)
    }

    @Test
    fun `parseBatch should handle rows with fewer columns`()
    {
        val inputBatch = listOf(
            listOf("2023-10-01T12:34:56", "INFO")
        )

        val result = parseBatch(inputBatch)

        assertEquals(1, result.size)
        assertEquals(LocalDateTime.parse("2023-10-01T12:34:56"), result[0].timeStamp)
        assertEquals(LogLevel.INFO, result[0].level)
        assertNull(result[0].source)
        assertNull(result[0].message)
        assertNull(result[0].detailMessage)
    }

    @Test
    fun `parseBatch should handle empty rows`()
    {
        val inputBatch: List<List<String>> = listOf(
            emptyList()
        )

        val result = parseBatch(inputBatch)

        assertEquals(1, result.size)
        assertNull(result[0].timeStamp)
        assertNull(result[0].level)
        assertNull(result[0].source)
        assertNull(result[0].message)
        assertNull(result[0].detailMessage)
    }

    @Test
    fun `parseBatch should handle empty input batch`()
    {
        val inputBatch = emptyList<List<String>>()

        val result = parseBatch(inputBatch)

        assertTrue(result.isEmpty())
    }
}