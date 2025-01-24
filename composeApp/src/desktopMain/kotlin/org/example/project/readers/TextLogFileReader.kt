package org.example.project.readers

import ILogFileReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class TextLogFileReader : ILogFileReader
{
    override fun process(filePath: String, batchSize: Int) = flow {
        val file = File(filePath)
        require(file.exists() && file.canRead()) { "File does not exist or cannot be read." }

        emit(
            file.useLines { sequence ->
                sequence.take(batchSize)
                    .map { it.split("\t") }
                    .toList()
            }
        )
    }.flowOn(Dispatchers.IO)
}