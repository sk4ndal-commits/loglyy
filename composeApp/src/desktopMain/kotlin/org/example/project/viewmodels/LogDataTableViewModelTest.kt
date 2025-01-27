package org.example.project.viewmodels

import ILogFileReader
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.example.project.data.FilterSettings
import org.example.project.data.LogRow
import org.example.project.providers.IReaderProvider
import org.example.project.providers.ReaderProvider
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class LogDataTableViewModelTest
{

    @Test
    fun `setFilePath should initialize filePath and start loading rows`() = runBlocking {
        val mockReaderProvider = mock<IReaderProvider>()
        val mockReader = mock<ILogFileReader>()
        whenever(mockReaderProvider.getReader(any())).thenReturn(mockReader)
        whenever(mockReader.process(any(), any())).thenReturn(flowOf(emptyList()))

        val viewModel = LogDataTableViewModel(mockReaderProvider)

        viewModel.setFilePath("testPath")

        assertEquals("testPath", viewModel.javaClass.getDeclaredField("filePath").apply { isAccessible = true }.get(viewModel))
        //assertFalse(viewModel.endOfFile.first())
        assertTrue(viewModel.isLoading.first())
    }

    @Test
    fun `loadInitialRows should correctly apply filters and update rows`() = runBlocking {
        val mockReaderProvider = mock<ReaderProvider>()
        val mockReader = mock<ILogFileReader>()
        val logRows = listOf(LogRow(message = "TestMessage1"), LogRow(message = "TestMessage2"))
        whenever(mockReaderProvider.getReader(any())).thenReturn(mockReader)
        whenever(mockReader.process(any(), any())).thenReturn(flowOf(logRows, emptyList()))

        val viewModel = LogDataTableViewModel(mockReaderProvider)
        viewModel.setFilePath("testFile.txt")

        val updatedRows = viewModel.rows.first()
        assertEquals(2, updatedRows.size)
        assertTrue(updatedRows.any { it.message == "TestMessage1" })
        assertTrue(updatedRows.any { it.message == "TestMessage2" })
        assertFalse(viewModel.isLoading.first())
    }

    @Test
    fun `loadMoreRows should append rows when not at end of file`() = runBlocking {
        val mockReaderProvider = mock<ReaderProvider>()
        val mockReader = mock<ILogFileReader>()
        val initialBatch = listOf(LogRow(message = "InitialMessage"))
        val additionalBatch = listOf(LogRow(message = "AdditionalMessage"))
        whenever(mockReaderProvider.getReader(any())).thenReturn(mockReader)
        whenever(mockReader.process(any(), any()))
            .thenReturn(flowOf(initialBatch, emptyList()))
            .thenReturn(flowOf(additionalBatch, emptyList()))

        val viewModel = LogDataTableViewModel(mockReaderProvider)
        viewModel.setFilePath("testFile.txt")

        val initialRows = viewModel.rows.first()
        assertEquals(1, initialRows.size)
        assertTrue(initialRows.any { it.message == "InitialMessage" })

        viewModel.loadMoreRows(FilterSettings())

        val updatedRows = viewModel.rows.first()
        assertEquals(2, updatedRows.size)
        assertTrue(updatedRows.any { it.message == "InitialMessage" })
        assertTrue(updatedRows.any { it.message == "AdditionalMessage" })
        assertFalse(viewModel.isLoading.first())
    }

    @Test
    fun `loadMoreRows should not load rows if already at end of file`() = runBlocking {
        val mockReaderProvider = mock<ReaderProvider>()
        val mockReader = mock<ILogFileReader>()
        val logRows = listOf(LogRow(message = "InitialMessage"))
        whenever(mockReaderProvider.getReader(any())).thenReturn(mockReader)
        whenever(mockReader.process(any(), any()))
            .thenReturn(flowOf(logRows, emptyList()))

        val viewModel = LogDataTableViewModel(mockReaderProvider)
        viewModel.setFilePath("testFile.txt")

        assertEquals(1, viewModel.rows.first().size)
        assertTrue(viewModel.rows.first().any { it.message == "InitialMessage" })

        viewModel.loadMoreRows(FilterSettings())
        viewModel.loadMoreRows(FilterSettings())

        assertEquals(1, viewModel.rows.first().size)
        assertTrue(viewModel.rows.first().any { it.message == "InitialMessage" })
        assertTrue(viewModel.endOfFile.first())
    }

    @Test
    fun `setFilePath should throw exception when filePath is empty`()
    {
        val mockReaderProvider = mock<ReaderProvider>()

        val viewModel = LogDataTableViewModel(mockReaderProvider)

        assertThrows<IllegalArgumentException> {
            viewModel.setFilePath("")
        }
    }

    @Test
    fun `applyFilters should filter rows based on filter settings`()
    {
        val mockReaderProvider = mock<ReaderProvider>()

        val rows = listOf(
            LogRow(message = "ERROR: Something went wrong"),
            LogRow(message = "INFO: All systems operational"),
            LogRow(message = "WARN: Low disk space")
        )

        val viewModel = LogDataTableViewModel(mockReaderProvider)
        val filterSettings = FilterSettings(filterText = "ERROR")

        val filteredRows = viewModel.javaClass
            .getDeclaredMethod("applyFilters", List::class.java, FilterSettings::class.java)
            .apply { isAccessible = true }
            .invoke(viewModel, rows, filterSettings) as List<*>

        assertEquals(1, filteredRows.size)
        assertTrue(filteredRows.any { (it as LogRow).message == "ERROR: Something went wrong" })
    }
}