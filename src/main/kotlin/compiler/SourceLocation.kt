package compiler

/**
 * SourceLocation Class: Represents a location in the source code
 *
 * @param fileName The name of the file.
 * @param lineNumber The line number.
 * @param columnNumber The column number.
 */
data class SourceLocation(var fileName: String = "", var lineNumber: Int = 0, var columnNumber: Int = 0) {
    override fun toString(): String {
        return "in file $fileName, line $lineNumber column $columnNumber"
    }
}