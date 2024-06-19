package compiler

/**
 * Token Interface: Represents a token
 *
 * @param location: location of the token
 */
interface Token {
    var location: SourceLocation
}

/**
 * KeyWordEnum: Derived from Token, represents keywords
 */
class KeyWordToken(var keywordEnum: KeyWordEnum, override var location: SourceLocation) : Token {
    override fun toString(): String {
        return "KeyWordToken('$keywordEnum')"
    }
}

/**
 * IdentifierToken: Derived from Token, represents identifiers
 */
class IdentifierToken(var identifier: String, override var location: SourceLocation) : Token {
    override fun toString(): String {
        return "IdentifierToken('$identifier')"
    }
}

/**
 * LiteralStringToken: Derived from Token, represents strings between double quotes
 */
class LiteralStringToken(var string: String, override var location: SourceLocation) : Token {
    override fun toString(): String {
        return "LiteralStringToken('$string')"
    }
}

/**
 * LiteralNumberToken: Derived from Token, represents numbers
 */
class LiteralNumberToken(var number: Float, override var location: SourceLocation) : Token {
    override fun toString(): String {
        return "LiteralNumberToken('$number')"
    }
}

/**
 * SymbolToken: Derived from Token, represents symbols
 */
class SymbolToken(var char: Char, override var location: SourceLocation) : Token {
    override fun toString(): String {
        return "SymbolToken('$char')"
    }
}

/**
 * StopToken: Derived from Token, represents the end of the file
 */
class StopToken(override var location: SourceLocation) : Token {
}
