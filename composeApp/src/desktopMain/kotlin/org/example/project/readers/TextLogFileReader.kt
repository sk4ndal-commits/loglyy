package org.example.project.readers

import ILogFileReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.example.project.data.parseBatch
import java.io.File

class TextLogFileReader : ILogFileReader
{
    /**
     * Reads a log file from the specified file path, processes it in batches of the given size, and emits parsed results as a flow of log entries.
     *
     * @param filePath The path to the log file to be read. The file must exist and be readable.
     * @param batchSize The number of lines to process in each batch before emitting the parsed results.
     * @return A flow emitting lists of `LogRow` objects, each representing a batch of parsed log entries.
     */
    override fun process(filePath: String, batchSize: Int) = flow {
        val file = File(filePath)
        require(file.exists() && file.canRead()) { "File does not exist or cannot be read." }

        file.useLines { lines ->
            val iterator = lines.iterator()
            var batch = mutableListOf<List<String>>()

            while (iterator.hasNext()) {
                val line = iterator.next()
                batch.add(line.split("\t"))

                // Once the batch reaches the batch size, parse and emit it
                if (batch.size == batchSize) {
                    emit(parseBatch(batch)) // Parse the batch and emit it
                    batch.clear() // Clear the batch for the next set of rows
                }
            }

            // Emit any remaining rows in the last batch
            if (batch.isNotEmpty()) {
                emit(parseBatch(batch))
            }
        }
    }.flowOn(Dispatchers.IO)
}