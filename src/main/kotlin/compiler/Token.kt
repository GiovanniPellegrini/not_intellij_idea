package compiler

/**
 * Tokens used to encode the file “scene.txt” , each token characterizes a different char that you can find in the file
 * “Location” is used to address the location in the text file in case of error
 *
 */

interface Token{
    var location:SourceLocation
}

/**
 * Used to identify any KeyWord, as 'Float,Sphere,Plane...'
 */
class KeyWordToken(var keywordEnum: KeyWordEnum,override var location: SourceLocation): Token{
}

/**
 * Used to identify any name which is not a KeyWord
 */
class IdentifierToken(var string: String, override var location: SourceLocation):Token{
}

/**
 * Used to identify any String between "  "
 */
class LiteralStringToken(var string: String, override var location: SourceLocation):Token{
}

/**
 * Used to identify any number
 */
class LiteralNumberToken(var number:Float, override var location: SourceLocation):Token {
}

/**
 * Used to identify symbols like '(),[],+...'
 */
class SymbolToken(var char:Char, override var location:SourceLocation):Token{

}

class StopToken(override var location: SourceLocation):Token{
}
