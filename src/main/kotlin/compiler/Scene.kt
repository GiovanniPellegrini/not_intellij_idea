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
import Box
import Color
import Plane
import ImagePigment
import OrthogonalCamera
import PerspectiveCamera
import Pigment
import Transformation
import Point
import DiffusionBRDF
import SpecularBRDF
import scalingTransformation
import UniformPigment
import readPfmImage
import java.io.FileInputStream
import java.io.InputStream


// to be developed
class Scene(
    val materials: MutableMap<String, Material> = mutableMapOf(),
    val world: World = World(),
    var camera: Camera? = null,
    var floatVariables: MutableMap<String, Float> = mutableMapOf(),
    var overriddenVariables: MutableSet<String> = mutableSetOf()
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

    fun parsePoint(inputStream: InStream): Point {
        expectSymbol(inputStream, "(")
        val x = expectNumber(inputStream)
        expectSymbol(inputStream, ",")
        val y = expectNumber(inputStream)
        expectSymbol(inputStream, ",")
        val z = expectNumber(inputStream)
        expectSymbol(inputStream, ")")

        return Point(x, y, z)
    }

    fun parsePigment(inputStream: InStream): Pigment {
        val keyWord =
            expectKeyWords(inputStream, listOf(KeyWordEnum.UNIFORM, KeyWordEnum.CHECKERED, KeyWordEnum.IMAGE))
        expectSymbol(inputStream, "(")

        val result: Pigment = when (keyWord) {
            KeyWordEnum.UNIFORM -> {
                val color = parseColor(inputStream)
                UniformPigment(color)
            }

            KeyWordEnum.CHECKERED -> {
                val color1 = parseColor(inputStream)
                expectSymbol(inputStream, ",")
                val color2 = parseColor(inputStream)
                expectSymbol(inputStream, ",")
                val numberOfSteps = expectNumber(inputStream)
                CheckeredPigment(color1, color2, numberOfSteps.toInt())
            }

            KeyWordEnum.IMAGE -> {
                val string = expectString(inputStream)
                val stream = FileInputStream(string)
                val image = readPfmImage(stream)
                ImagePigment(image)
            }

            else -> throw Error("no clear definition of Pigment")
        }

        expectSymbol(inputStream, ")")
        return result
    }

    fun parseBrdf(inputStream: InStream): BRDF {
        val kw = expectKeyWords(inputStream, listOf(KeyWordEnum.DIFFUSE, KeyWordEnum.SPECULAR))
        expectSymbol(inputStream, "(")
        val pigment = parsePigment(inputStream)
        expectSymbol(inputStream, ")")
        return when (kw) {
            KeyWordEnum.DIFFUSE -> DiffusionBRDF(pigment)
            KeyWordEnum.SPECULAR -> SpecularBRDF(pigment)
            else -> throw GrammarError(inputStream.location, "no clear definition of BRDF")
        }
    }

    fun parseMaterial(inputStream: InStream): Map<String, Material> {
        val materialName = expectIdentifier(inputStream)
        expectSymbol(inputStream, "(")
        val brdf = parseBrdf(inputStream)
        expectSymbol(inputStream, ",")
        val emittedRad = parsePigment(inputStream)
        expectSymbol(inputStream, ")")

        return mapOf(materialName to Material(brdf, emittedRad))
    }

    fun parseSphere(inStream: InStream): Sphere {
        expectSymbol(inStream, "(")
        val materialName = expectIdentifier(inStream)
        if (materialName !in materials.keys) {
            throw GrammarError(inStream.location, "unknown material $materialName")
        }
        expectSymbol(inStream, ",")
        val transformation = parseTransformation(inStream)
        expectSymbol(inStream, ")")

        return Sphere(transformation = transformation, material = materials[materialName]!!)
    }

    fun parsePlane(inStream: InStream): Plane {
        expectSymbol(inStream, "(")
        val materialName = expectIdentifier(inStream)
        if (materialName !in materials.keys) {
            throw GrammarError(inStream.location, "unknown material $materialName")
        }
        expectSymbol(inStream, ",")
        val transformation = parseTransformation(inStream)
        expectSymbol(inStream, ")")

        return Plane(transformation = transformation, material = materials[materialName]!!)
    }

    fun parseBox(inputStream: InStream): Box {
        expectSymbol(inputStream, "(")
        val Pmin = parsePoint(inputStream)
        expectSymbol(inputStream, ",")
        val PMax = parsePoint(inputStream)
        expectSymbol(inputStream, ")")
        val materialName = expectIdentifier(inputStream)
        if (materialName !in materials) {
            throw GrammarError(inputStream.location, "unknown material $materialName")
        }
        expectSymbol(inputStream, ",")
        val transformation = parseTransformation(inputStream)
        expectSymbol(inputStream, ")")

        return Box(Pmax = PMax, Pmin = Pmin, transformation = transformation, material = materials[materialName]!!)

    }

    fun parseTransformation(inputStream: InStream): Transformation {
        var result = Transformation()
        while (true) {
            val transformationKw = expectKeyWords(
                inputStream, listOf(
                    KeyWordEnum.IDENTITY,
                    KeyWordEnum.TRANSLATION,
                    KeyWordEnum.ROTATION_X,
                    KeyWordEnum.ROTATION_Y,
                    KeyWordEnum.ROTATION_Z
                )
            )

            when (transformationKw) {
                KeyWordEnum.IDENTITY -> {
                    //do nothing
                }

                KeyWordEnum.TRANSLATION -> {
                    expectSymbol(inputStream, "(")
                    result *= Translation(parseVector(inputStream))
                    expectSymbol(inputStream, ")")
                }

                KeyWordEnum.ROTATION_X -> {
                    expectSymbol(inputStream, "(")
                    val angle = expectNumber(inputStream)
                    result *= Rotation((Vector(1f, 0f, 0f)), angle)
                    expectSymbol(inputStream, ")")
                }

                KeyWordEnum.ROTATION_Y -> {
                    expectSymbol(inputStream, "(")
                    val angle = expectNumber(inputStream)
                    result *= Rotation((Vector(0f, 1f, 0f)), angle)
                    expectSymbol(inputStream, ")")
                }

                KeyWordEnum.ROTATION_Z -> {
                    expectSymbol(inputStream, "(")
                    val angle = expectNumber(inputStream)
                    result *= Rotation((Vector(0f, 0f, 1f)), angle)
                    expectSymbol(inputStream, ")")
                }

                KeyWordEnum.SCALING -> {
                    expectSymbol(inputStream, "(")
                    val scaling = parseVector(inputStream)
                    result *= scalingTransformation(scaling)
                    expectSymbol(inputStream, ")")
                }

                else -> throw RuntimeException("no clear definition of Transformation")
            }
            // read next token to check if there is another transformation chained
            val nextKw = inputStream.readToken()
            if (nextKw !is SymbolToken || nextKw.char != '*') {
                inputStream.unreadToken(nextKw)
                break
            }
        }
        return result
    }

    fun parseCamera(inputStream: InStream): Camera {
        expectSymbol(inputStream, "(")
        val typeKw = expectKeyWords(inputStream, listOf(KeyWordEnum.ORTHOGONAL, KeyWordEnum.PERSPECTIVE))
        expectSymbol(inputStream, ",")


        //if not defined, other arguments are assigned to default values

        val transformation = try {
            parseTransformation(inputStream)
        } catch (e: GrammarError) {
            Transformation()
        }

        expectSymbol(inputStream, ",")

        val aspectRatio = try {
            expectNumber(inputStream)
        } catch (e: GrammarError) {
            1f
        }

        expectSymbol(inputStream, ",")

        val distance = try {
            expectNumber(inputStream)
        } catch (e: GrammarError) {
            1f
        }

        expectSymbol(inputStream, ")")

        return when (typeKw) {
            KeyWordEnum.ORTHOGONAL -> {
                OrthogonalCamera(aspectRatio, transformation)
            }

            KeyWordEnum.PERSPECTIVE -> {
                PerspectiveCamera(distance, aspectRatio, transformation)
            }

            else -> {
                throw GrammarError(inputStream.location, "no clear definition of Camera")
            }
        }
    }

    /**
     * Reads a scene description from the input stream and returns a Scene object
     */
    fun parseScene(inputStream: InStream, variables: MutableMap<String, Float> = mutableMapOf()): Scene {
        val scene = Scene()
        scene.floatVariables = variables
        scene.overriddenVariables = variables.keys

        while (true) {
            val what = inputStream.readToken()
            if (what is StopToken) {
                break
            }
            if (what !is KeyWordToken) {
                throw GrammarError(what.location, "Expected a keyword, but got $what")
            }
            when (what.keywordEnum) {
                KeyWordEnum.FLOAT -> {
                    val variableName = expectIdentifier(inputStream)
                    val variableLocation = inputStream.location

                    expectSymbol(inputStream, "(")
                    val varValue = expectNumber(inputStream)
                    expectSymbol(inputStream, ")")
                    // Error if VariableName already defined
                    if (variableName in scene.overriddenVariables && variableName !in scene.overriddenVariables) {
                        throw GrammarError(variableLocation, "Variable '$variableName' cannot be redefined")
                    }
                    // Define variable if not already defined before
                    if (variableName !in scene.overriddenVariables) {
                        scene.floatVariables[variableName] = varValue
                    }


                }

                KeyWordEnum.SPHERE -> {
                    scene.world.add(parseSphere(inputStream))
                }

                KeyWordEnum.PLANE -> {
                    scene.world.add(parsePlane(inputStream))
                }

                KeyWordEnum.CAMERA -> {
                    if (camera != null) {
                        throw GrammarError(what.location, "Camera already defined")
                    }
                    camera = parseCamera(inputStream)
                }

                KeyWordEnum.MATERIAL -> {
                    scene.materials.putAll(parseMaterial(inputStream))
                }

                else -> {
                    throw GrammarError(what.location, "Unexpected token ${what}")
                }
            }
        }
        return scene
    }
}


