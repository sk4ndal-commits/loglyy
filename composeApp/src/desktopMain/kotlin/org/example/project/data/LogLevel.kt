package org.example.project.data

/**
 * Represents the various logging levels that can be assigned to log events or entries.
 *
 * TRACE: Designates fine-grained information, typically for diagnosing issues and tracing the execution of code.
 * DEBUG: Used for detailed information valuable during the debugging of applications.
 * INFO: General operational messages providing information on the application's normal behavior.
 * WARN: Indicates potential issues or situations that require attention but do not disrupt normal operation.
 * ERROR: Represents serious issues that prevent parts of the application from functioning correctly.
 */
enum class LogLevel {
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR;
}