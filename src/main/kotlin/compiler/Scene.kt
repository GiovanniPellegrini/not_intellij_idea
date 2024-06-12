package compiler
import Material
import World
import Camera

// to be developed
class Scene(
    val materials: MutableMap<String, Material>,
    val world: World = World(),
    val camera: Camera? = null,
    val floatVariables: MutableMap<String, Float>,
    val overriddenVariables: MutableSet<String>
){
    /**
     * Reads a token and checks if it is a symbol
     */
    fun expectSymbol(inputFile: InStream, symbol: String){
        val token = inputFile.readToken()
        if(token !is SymbolToken){
            throw GrammarError(token.location, "Expected symbol $symbol, but got $token")
        }
    }

    /**
     * Reads a token and checks if it is a keyword,
     * @return the keyword that was read as a KeyWordEnum
     */
    fun expectKeywords(inputFile: InStream, keywords: List<KeyWordEnum>): KeyWordEnum{
        val token = inputFile.readToken()
        if(token !is KeyWordToken){
            throw GrammarError(token.location, "Expected a keyword, but got $token")
        }
        if(token.keywordEnum !in keywords){
            throw GrammarError(token.location, "Expected one of $keywords, but got ${token.keywordEnum}")
        }
        return token.keywordEnum
    }

    /**
     * Reads a token and checks if it is a literal number or a variable
     * @return the number that was read
     */
    fun expectNumber(inputFile: InStream, scene: Scene): Float {
        val token = inputFile.readToken()
        if(token is LiteralNumberToken){
            return token.number
        }else if(token is IdentifierToken){
            val variableName = token.string
            if(variableName !in scene.floatVariables){
                throw GrammarError(token.location, "Unknown variable $variableName")
            }
            return scene.floatVariables[variableName]!!
        }
        throw GrammarError(token.location, "Expected a number, but got $token")
    }
}
