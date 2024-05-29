package compiler
import java.io.InputStream
import kotlin.Char as Char

class InputStream(val stream: InputStream, val fileName: String = "", val tabulation: Int = 8,
                  var location: SourceLocation = SourceLocation(fileName = fileName, lineNumber = 1, columnNumber = 1),
                  var savedChar: Char = '\u0000', var savedLocation: SourceLocation = location){

    /**
     * update the position of the lexer after reading a character from the stream, '\u0000' is the null character
     */
    fun updatePos(c: Char){
        when(c){
            '\u0000' -> return
            '\n' -> {
                location.lineNumber++
                location.columnNumber = 1
            }
            '\t' -> {
                location.columnNumber += tabulation
            }
            else -> {
                location.columnNumber++
            }
        }
    }

    /**
     * readChar reads a character from the stream and updates the position of the lexer
     */
    fun readChar(): Char {
        var c: Char
        c = stream.read().toChar()
        if(savedChar != '\u0000'){
            c = savedChar
            savedChar = '\u0000'
        }
        else{
            c = stream.read().toChar()
        }
        savedLocation = location.copy()
        updatePos(c)
        return c
    }

    fun unreadChar(c: Char) {
        assert(savedChar == '\u0000')
        savedChar = c
        location = savedLocation.copy()
    }
}