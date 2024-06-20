package compiler

import java.io.File

/**
 * GrammarError Class: Derived from RuntimeException, represents a grammar error in the source code.
 *
 * @param location: location of the error
 * @param message:  error message
 */
class GrammarError(private val location: SourceLocation, message: String) : RuntimeException(message) {
    override fun toString(): String {
        val fileLines = File(location.fileName).readLines()
        val errorLine = fileLines[location.lineNumber - 1]
        val underline = " ".repeat(location.columnNumber - 1) + "\u001B[31m^^^\u001B[0m"
        return "\n\n$message $location \n\n$errorLine\n$underline"
    }
}