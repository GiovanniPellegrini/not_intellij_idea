package compiler

/**
 * Represents a location in the source code.
 *
 * @property fileName The name of the file.
 * @property lineNumber The line number.
 * @property columnNumber The column number.
 */
data class SourceLocation(var fileName: String = "", var lineNumber: Int = 0, var columnNumber: Int = 0){}