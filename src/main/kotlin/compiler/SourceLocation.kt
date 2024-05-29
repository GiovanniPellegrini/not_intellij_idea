package compiler

/**
 * Represents a location in the source code.
 *
 * @property fileName The name of the file.
 * @property lineNumber The line number.
 * @property columnNumber The column number.
 */
data class SourceLocation(val fileName: String = "", val lineNumber: Int = 0, val columnNumber: Int = 0){}