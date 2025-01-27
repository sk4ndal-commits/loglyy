package org.example.project.providers

import ILogFileReader

interface IReaderProvider
{
    fun getReader(filePath: String): ILogFileReader
}