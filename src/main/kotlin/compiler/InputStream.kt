package compiler

import java.io.InputStreamReader
import kotlin.Char as Char

private const val symbols = "()<>,*=[]"
private const val WHITESPACE = " \t\n\r"

/**
 *  InStream Class: Responsible for lexing and parsing files that define scenes.
 *
 *  @param stream: InputStreamReader object that reads the file
 *  @param fileName: name of the file
 *  @param tabulation: number of spaces that a tabulation represents
 *  @param location: location of the lexer in the file
 *  @param savedChar: character saved
 *  @param savedLocation: location of the saved character
 *  @param saveToken: token saved
 */
class InStream(
    val stream: InputStreamReader, val fileName: String = "", private val tabulation: Int = 4,
    var location: SourceLocation = SourceLocation(fileName = fileName, lineNumber = 1, columnNumber = 1),
    private var savedChar: Char = '\u0000', private var savedLocation: SourceLocation = location,
    private var saveToken: Token? = null
) {

    /**
     * Update the position of the lexer after reading a character from the stream
     */
    private fun updatePos(c: Char) {
        // in kotlin char must be written between single quotes
        when (c) {
            '\u0000' -> return
            '\n' -> {
                location.lineNumber++
                location.columnNumber = 1
            }

            '\t' -> location.columnNumber += tabulation

            else -> location.columnNumber++
        }
    }

    /**
     * Reads a character from the stream and updates the position of the lexer
     */
    fun readChar(): Char {
        val c: Char
        if (savedChar != '\u0000') {
            c = savedChar
            savedChar = '\u0000'
        } else {
            val isEof = stream.read()
            // if isEof is -1 it means that the end of the file has been reached and return '\u0000'
            c = if (isEof != -1) {
                isEof.toChar()
            } else '\u0000'
        }
        savedLocation = location.copy()
        updatePos(c)
        return c
    }

    /**
     * Records a character in savedChar and updates the position of the lexer
     */
    fun unreadChar(c: Char) {
        assert(savedChar == '\u0000')
        savedChar = c
        location = savedLocation.copy()
    }


    /**
     * Skips all the white spaces and comments in the file
     */
    fun skipWhiteSpace() {
        var c = readChar()
        while (c in WHITESPACE || c == '%' || c == '^') {
            // '%' is the comment character
            if (c == '%') {
                while (readChar() !in listOf('\u0000', '\n', '\r')) {
                    continue
                }
            }
            // '^' is the long-comment character
            if (c == '^') {
                while (readChar() != '^') {
                    continue
                }
            }
            c = readChar()
            if (c == '\u0000') {
                return
            }
        }
        unreadChar(c)
    }


    /**
     * Reads the stream and returns a specific type of token
     */
    fun readToken(): Token {

        if (saveToken != null) {
            val result: Token = saveToken!!
            saveToken = null
            return result
        }

        this.skipWhiteSpace()
        val c = this.readChar()
        // if char is equal to '' we are at the end of the file
        if (c == '\u0000') return StopToken(location = location)

        //record the position into another variable to separate from this.location
        val tokenLocation = this.location.copy()

        //if char is equal to Symbol, return SymbolToken
        return if (c in symbols) SymbolToken(c, tokenLocation)

        //if char is '"', it means that is the beginning of a filename
        else if (c == '"') parseStringToken(tokenLocation)

        //if char is a number or an operation return that number
        else if (c.isDigit() || c in charArrayOf('+', '-', '.')) parseFloatToken(c, tokenLocation)

        //if char is a letter, it can be a KeyWord or a name. So it returns KeyWordToken/IdentifierToken
        else if (c.isLetter() || c == '_') parseWordOrKeyToken(c.toString(), tokenLocation)
        else throw GrammarError(tokenLocation, "invalid character $c")
    }

    /**
     * Unreads a token saving it in saveToken
     */
    fun unreadToken(token: Token) {
        assert(saveToken == null)
        saveToken = token
    }

    /**
     * Reads a string contained between the quotation marks “ ”
     */
    private fun parseStringToken(tokenLocation: SourceLocation): LiteralStringToken {
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
     * Reads a Float number until it finds something different from a number, '.' or 'E,e'
     */
    private fun parseFloatToken(c: Char, tokenLocation: SourceLocation): LiteralNumberToken {
        var string: String = c.toString()
        while (true) {
            val ch = readChar()

            if (!ch.isDigit() && ch != '.' && ch !in charArrayOf('E', 'e')) {
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

    /**
     * Reads a word and returns a KeyWordToken if it is KeyWord, else returns IdentifierToken
     */
    private fun parseWordOrKeyToken(firstC: String, tokenLocation: SourceLocation): Token {
        var token = firstC
        var c: Char
        while (true) {
            c = this.readChar()
            if (c == '\u0000') break
            if (!(c.isLetterOrDigit() || c == '_')) {
                this.unreadChar(c)
                break
            }

            token += c
        }

        return if (KEYWORDS.containsKey(token)) KeyWordToken(KEYWORDS[token]!!, tokenLocation)
        else IdentifierToken(token, tokenLocation)
    }
}

