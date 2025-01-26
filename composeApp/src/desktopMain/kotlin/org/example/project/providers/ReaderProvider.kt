package org.example.project.providers

import ILogFileReader
import org.example.project.readers.TextLogFileReader
import java.nio.file.Path

/**
 * Provides an instance of an appropriate log file reader based on the file type.
 *
 * This class is responsible for determining the correct implementation of the
 * ILogFileReader interface based on the provided file path. Currently, it supports
 * plain text log files with the `.txt` extension.
 *
 * @throws IllegalArgumentException if the provided file path does not represent
 * a supported file type.
 */
class ReaderProvider
{
    /**
     * Provides an appropriate log file reader instance based on the given file path.
     *
     * Determines the correct implementation of the ILogFileReader interface that
     * matches the provided file path. This function currently supports only `.txt`
     * files and throws an exception otherwise.
     *
     * @param filePath The path to the log file for which a reader implementation is required.
     * @return An instance of ILogFileReader suitable for processing the specified file type.
     * @throws IllegalArgumentException If the file type specified in the file path is not supported.
     */
    fun getReader(filePath: String): ILogFileReader
    {
        return if (filePath.endsWith(".txt")) TextLogFileReader() else throw IllegalArgumentException("Unsupported file type.")
    }
}