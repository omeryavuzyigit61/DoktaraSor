package com.dombikpanda.doktarasor.filelogger

import java.io.File

interface FileLogger {

    fun printLogs(){

    }

    open fun logError(error: String) {
        val file = File("errors.txt")
        file.appendText(
            text = error
        )
    }
}

class CustomErrorFileLogger : FileLogger {
    override fun logError(error: String) {
        val file = File("my_custom_error_file.txt")
        file.appendText(
            text = error
        )
    }
}
