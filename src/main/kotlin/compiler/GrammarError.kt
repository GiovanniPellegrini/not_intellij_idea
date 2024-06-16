package compiler
import java.io.File

class GrammarError(val location: SourceLocation, message: String) : RuntimeException(message) {
    override fun toString(): String {
        val fileLines = File(location.fileName).readLines()
        val errorLine = fileLines[location.lineNumber - 1]
        val underline = " ".repeat(location.columnNumber - 1) + "\u001B[31m^^^\u001B[0m"
        return "\n$message \n$location \n\n$errorLine\n$underline"
    }
}