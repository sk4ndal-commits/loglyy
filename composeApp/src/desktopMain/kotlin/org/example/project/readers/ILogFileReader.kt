import kotlinx.coroutines.flow.Flow

interface ILogFileReader {
    /**
     * Reads log file asynchronously in batches.
     *
     * @param filePath: The path to the log file.
     * @param batchSize: Number of lines to read in each batch.
     * @return: A Flow<List<List<String>>> emitting batches of rows.
     */
    fun process(filePath: String, batchSize: Int): Flow<List<List<String>>>
}