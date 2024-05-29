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
        // in kotlin char must be written between single quotes
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
        val c: Char
        if(savedChar != '\u0000'){
            c = savedChar
            savedChar = '\u0000'
        }
        else{
            val isEof = stream.read()
            // if isEof is -1 it means that the end of the file has been reached and return '\u0000'
            c = if(isEof != -1){
                isEof.toChar()
            }else '\u0000'
        }
        savedLocation = location.copy()
        updatePos(c)
        return c
    }

    /**
     * unreadChar saves a character in savedChar and updates the position of the lexer
     */
    fun unreadChar(c: Char) {
        assert(savedChar == '\u0000')
        savedChar = c
        location = savedLocation.copy()
    }

    private val WHITESPACE = " \t\n\r"

    fun skipWhiteSpace(){
        var c = readChar()
        while(c in WHITESPACE || c == '%'){ // '%' is the comment character
            if(c == '%'){
                while(readChar() !in listOf('\u0000', '\n', '\r')){
                    continue
                }
            }
            c = readChar()
            if(c == '\u0000'){
                return
            }
        }
        unreadChar(c)
    }
}