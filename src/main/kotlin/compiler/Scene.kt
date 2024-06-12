package compiler

import BRDF
import Sphere
import Translation
import Rotation
import Material
import World
import Camera
import CheckeredPigment
import Vector
import Color
import ImagePigment
import Pigment
import Transformation
import scalingTransformation
import UniformPigment
import readPfmImage
import java.io.FileInputStream



// to be developed
class Scene(
    val materials: MutableMap<String, Material>,
    val world: World = World(),
    val camera: Camera? = null,
    val floatVariables: MutableMap<String, Float>,
    val overriddenVariables: MutableSet<String>
) {
    /**
     * Reads a token and checks if it is a symbol
     */
    fun expectSymbol(inputFile: InStream, symbol: String) {
        val token = inputFile.readToken()
        if (token !is SymbolToken) {
            throw GrammarError(token.location, "Expected symbol $symbol, but got $token")
        }
    }

    /**
     * Reads a token and checks if it is a keyword,
     * @return the keyword that was read as a KeyWordEnum
     */
    fun expectKeyWords(inputFile: InStream, keywords: List<KeyWordEnum>): KeyWordEnum {
        val token = inputFile.readToken()
        if (token !is KeyWordToken) {
            throw GrammarError(token.location, "Expected a keyword, but got $token")
        }
        if (token.keywordEnum !in keywords) {
            throw GrammarError(token.location, "Expected one of $keywords, but got ${token.keywordEnum}")
        }
        return token.keywordEnum
    }

    /**
     * Reads a token and checks if it is a literal number or a variable
     * @return the number that was read
     */
    fun expectNumber(inputFile: InStream): Float {
        val token = inputFile.readToken()
        if (token is LiteralNumberToken) {
            return token.number
        } else if (token is IdentifierToken) {
            val variableName = token.identifier
            if (variableName !in this.floatVariables) {
                throw GrammarError(token.location, "Unknown variable $variableName")
            }
            return this.floatVariables[variableName]!!
        }
        throw GrammarError(token.location, "Expected a number, but got $token")
    }


    /**
     * Reads a token and checks if it is a literal string
     * @return the string that was read
     */
    fun expectString(inputFile: InStream): String {
        val token = inputFile.readToken()
        if (token is LiteralStringToken) {
            return token.string
        }
        throw GrammarError(token.location, "Expected a string, but got $token")
    }

    /**
     * Reads a token and checks if it is an identifier
     * @return the identifier that was read
     */
    fun expectIdentifier(inputFile: InStream): String {
        val token = inputFile.readToken()
        if (token is IdentifierToken) {
            return token.identifier
        }
        throw GrammarError(token.location, "Expected an identifier, but got $token")
    }

    fun parseVector(inStream: InStream): Vector {
        expectSymbol(inStream, "<")
        val x = expectNumber(inStream)
        expectSymbol(inStream, ",")
        val y = expectNumber(inStream)
        expectSymbol(inStream, ",")
        val z = expectNumber(inStream)
        expectSymbol(inStream, ">")

        return Vector(x, y, z)
    }

    fun parseColor(inStream: InStream): Color {
        expectSymbol(inStream, "<")
        val r = expectNumber(inStream)
        expectSymbol(inStream, ",")
        val g = expectNumber(inStream)
        expectSymbol(inStream, ",")
        val b = expectNumber(inStream)
        expectSymbol(inStream, ">")

        return Color(r, g, b)
    }

    fun parsePigment(inStream: InStream): Pigment {
        val keyWord =
            expectKeyWords(inStream, listOf(KeyWordEnum.UNIFORM, KeyWordEnum.CHECKERED, KeyWordEnum.IMAGE))
        expectSymbol(inStream, "(")

        val result: Pigment = when (keyWord) {
            KeyWordEnum.UNIFORM -> {
                val color = parseColor(inStream)
                UniformPigment(color)
            }

            KeyWordEnum.CHECKERED -> {
                val color1 = parseColor(inStream)
                expectSymbol(inStream, ",")
                val color2 = parseColor(inStream)
                expectSymbol(inStream, ",")
                val numberOfSteps = expectNumber(inStream)
                CheckeredPigment(color1, color2, numberOfSteps.toInt())
            }

            KeyWordEnum.IMAGE -> {
                val string = expectString(inStream)
                val stream = FileInputStream(string)
                val image = readPfmImage(stream)
                ImagePigment(image)
            }

            else -> throw Error("no clear definition of Pigment")
        }

        expectSymbol(inStream, ")")
        return result
    }

    fun parseBrdf(inStream: InStream):BRDF{
        TODO()
    }

    fun parseMaterial(inStream: InStream): Map<String, Material>{
        val materialName = expectIdentifier(inStream)
        expectSymbol(inStream, "(")
        val brdf = parseBrdf(inStream)
        expectSymbol(inStream, ",")
        val emittedRad = parsePigment(inStream)
        expectSymbol(inStream, ")")

        return mapOf(materialName to Material(brdf, emittedRad))
    }

    fun parseTransformation(inStream: InStream){
        var result = Transformation()
        while(true){
            val transformationKw = expectKeyWords(inStream,listOf(
                 KeyWordEnum.IDENTITY,
                 KeyWordEnum.TRANSLATION,
                 KeyWordEnum.ROTATION_X,
                 KeyWordEnum.ROTATION_Y,
                 KeyWordEnum.ROTATION_Z,))

            when(transformationKw){
                KeyWordEnum.IDENTITY -> {
                    continue
                }
                KeyWordEnum.TRANSLATION -> {
                    expectSymbol(inStream,"(")
                    result *= Translation(parseVector(inStream))
                    expectSymbol(inStream,")")
                }
                KeyWordEnum.ROTATION_X -> {
                    expectSymbol(inStream,"(")
                    val angle = expectNumber(inStream)
                    result *= Rotation((Vector(1f,0f,0f)),angle)
                    expectSymbol(inStream,")")
                }
                KeyWordEnum.ROTATION_Y -> {
                    expectSymbol(inStream,"(")
                    val angle = expectNumber(inStream)
                    result *= Rotation((Vector(0f,1f,0f)),angle)
                    expectSymbol(inStream,")")
                }
                KeyWordEnum.ROTATION_Z -> {
                    expectSymbol(inStream,"(")
                    val angle = expectNumber(inStream)
                    result *= Rotation((Vector(0f,0f,1f)),angle)
                    expectSymbol(inStream,")")
                }
                KeyWordEnum.SCALING -> {
                    expectSymbol(inStream,"(")
                    val scaling = parseVector(inStream)
                    result *= scalingTransformation(scaling)
                    expectSymbol(inStream,")")
                }
                else -> throw Error("no clear definition of Transformation")
            }
                TODO()
        }
    }


}


