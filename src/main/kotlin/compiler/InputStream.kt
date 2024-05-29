package compiler
import java.io.InputStream
import java.security.KeyException
import kotlin.Char as Char

class InputStream(val stream: InputStream, val fileName: String = "", val tabulation: Int = 8,
                  var location: SourceLocation = SourceLocation(fileName = fileName, lineNumber = 1, columnNumber = 1),
                  var savedChar: Char = '\u0000', var savedLocation: SourceLocation = location) {

    /**
     * update the position of the lexer after reading a character from the stream, '\u0000' is the null character
     */
    fun updatePos(c: Char) {
        // in kotlin char must be written between single quotes
        when (c) {
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
        if (savedChar != '\u0000') {
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



    private val symbols = "()<>,*="
    fun readToken():Token {
        this.skipWhiteSpace()
        val c = this.readChar()
        // if char is equal to '' we are at the end of the file
        if (c == '\u0000') return StopToken(location = location)

        //record the position into another variable to separate from this.location
        val tokenLocation = this.location.copy()

        //if char is equal to Symbol, return SymbolToken
        if (c in symbols) return SymbolToken(c, tokenLocation)

        //if char is '"', it means that is the starting of a filename
        else if (c == '"') return parseStringToken(tokenLocation)

        //if char is a number or an operation return that number
        else if (c.isDigit() || c in charArrayOf('+', '-', '.')) return parseFloatToken(c, tokenLocation)

        //if char is a letter, it can be a KeyWord or a name. So it returns KeyWordToken/IdentifierToken
        else if (c.isLetter() || c == '_') return parseWordOrKeyToken(c, tokenLocation)

        else throw GrammarError(tokenLocation, "invalid character $c")
    }


    /**
     * reads an entire string contained between the quotation marks “ ”
     */
    fun parseStringToken(tokenLocation: SourceLocation): LiteralStringToken {
        var string = ""
        while (true) {
            val c = this.readChar()

            if (c == '"') break
            if (c == '\u0000') throw GrammarError(tokenLocation, "string without end")

            string += c

        }
        return LiteralStringToken(string, tokenLocation)
    }

    /**
     * reads an Float number
     */
    fun parseFloatToken(c: Char, tokenLocation: SourceLocation): LiteralNumberToken {
        var string: String = c.toString()
        while (true) {
            val ch = readChar()

            if (!ch.isDigit() || ch != '.' || ch !in charArrayOf('E', 'e')) {
                this.unreadChar(ch)
                break
            }
            string += ch
        }

        try {
            string.toFloat()
        } catch (e: Error) {
            throw GrammarError(tokenLocation, "invalid float")
        }
        return LiteralNumberToken(string.toFloat(), tokenLocation)
    }


    fun parseWordOrKeyToken(c: Char,tokenLocation: SourceLocation): Token{
        var token = c.toString()
        while(true){
            var c = this.readChar()
            if(!c.isLetterOrDigit() || c == '_'){
                this.unreadChar(c)
                break
            }

            token+=c
        }

        return try {
            KeyWordToken(tokenLocation)
        }catch(e: KeyException){
            IdentifierToken(token, tokenLocation)
        }
    }


}

