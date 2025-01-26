import kotlinx.coroutines.flow.Flow
import org.example.project.data.LogRow

/**
 * Interface defining the contract for reading log files asynchronously in batches.
 *
 * Implementations of this interface should provide functionality to read log files
 * in a manner that supports asynchronous processing and emits the log content in
 * manageable batches.
 */
interface ILogFileReader {
    /**
     * Reads log file asynchronously in batches.
     *
     * @param filePath: The path to the log file.
     * @param batchSize: Number of lines to read in each batch.
     * @return: A Flow<List<List<String>>> emitting batches of rows.
     */
    fun process(filePath: String, batchSize: Int): Flow<List<LogRow>>
}