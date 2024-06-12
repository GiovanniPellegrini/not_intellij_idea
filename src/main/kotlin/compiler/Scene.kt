package compiler

import BRDF
import CSGDifference
import CSGIntersection
import Translation
import Rotation
import CSGUnion
import Material
import World
import Camera
import CheckeredPigment
import Vector
import Color
import DiffusionBRDF
import ImagePigment
import OrthogonalCamera
import PerspectiveCamera
import Pigment
import SpecularBRDF
import Transformation
import scalingTransformation
import UniformPigment
import readPfmImage
import java.io.FileInputStream
import Sphere
import Plane
import Shape

// to be developed
class Scene(
    val materials: MutableMap<String, Material>,
    val world: World = World(),
    val camera: Camera? = null,
    val floatVariables: MutableMap<String, Float>,
    val shapeVariables: MutableMap<String,Shape>,
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
    /**
     * Reads a token and check if it is the identifier of a shape
     * @return the shape that was read
     */
    fun expectShape(inputFile: InStream):Shape{
        val token=inputFile.readToken()
        if(token is IdentifierToken){
            val identifier=token.identifier
            if(identifier !in this.shapeVariables ){
                throw GrammarError(token.location, "Unknown variable $identifier")
            }
        return this.shapeVariables[identifier]!!
        }
        throw GrammarError(token.location, "Expected a shape, but got $token")
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

            else -> throw GrammarError(inStream.location, "no clear definition of Pigment")
        }

        expectSymbol(inStream, ")")
        return result
    }

    fun parseBrdf(inStream: InStream): BRDF {
        val brdfKeyWord = expectKeyWords(inStream, listOf(KeyWordEnum.DIFFUSE, KeyWordEnum.SPECULAR))
        expectSymbol(inStream, "(")
        val pigment = parsePigment(inStream)

        val result: BRDF = when (brdfKeyWord) {
            KeyWordEnum.DIFFUSE -> {
                DiffusionBRDF(pigment)
            }

            KeyWordEnum.SPECULAR -> {
                SpecularBRDF(pigment)
            }

            else -> throw GrammarError(inStream.location, "No clear definition of brdf")

        }
        return result
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

    fun parseTransformation(inStream: InStream):Transformation{
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
        }
    }

    fun parseSphere(inputFile: InStream): Sphere {
        expectSymbol(inputFile, "(")
        val transformation = parseTransformation(inputFile)
        expectSymbol(inputFile, ",")
        val materialName = expectIdentifier(inputFile)
        if (materialName !in materials)
            throw GrammarError(inputFile.location, "unknown material $materialName")

        expectSymbol(inputFile, ")")
        return Sphere(transformation = transformation, material = materials[materialName]!!)
    }

    fun parsePlane(inputFile: InStream): Plane {
        expectSymbol(inputFile, "(")
        val transformation = parseTransformation(inputFile)
        expectSymbol(inputFile, ",")
        val materialName = expectIdentifier(inputFile)
        if (materialName !in materials)
            throw GrammarError(inputFile.location, "unknown material $materialName")

        expectSymbol(inputFile, ")")
        return Plane(transformation = transformation, material = materials[materialName]!!)
    }

    fun parseCamera(inputFile: InStream): Camera {
        expectSymbol(inputFile, "(")
        val cameraKeyWord = expectKeyWords(inputFile, listOf(KeyWordEnum.PERSPECTIVE, KeyWordEnum.ORTHOGONAL))
        expectSymbol(inputFile, ",")
        val transformation = parseTransformation(inputFile)
        expectSymbol(inputFile, ",")
        val aspectRatio = expectNumber(inputFile)
        expectSymbol(inputFile, ",")
        val distance = expectNumber(inputFile)

        val result: Camera = when (cameraKeyWord) {
            KeyWordEnum.PERSPECTIVE -> {
                PerspectiveCamera(distance, aspectRatio, transformation)
            }

            KeyWordEnum.ORTHOGONAL -> {
                OrthogonalCamera(aspectRatio, transformation)
            }

            else -> throw GrammarError(inputFile.location, "Invalid type of camera")
        }
        return result
    }




    fun parseCSGUnion(inputFile: InStream):CSGUnion{
        expectSymbol(inputFile,"(")
        val shape1=expectShape(inputFile)
        expectSymbol(inputFile,",")
        val shape2=expectShape(inputFile)
        expectSymbol(inputFile,",")
        val transformation=parseTransformation(inputFile)
        expectSymbol(inputFile,",")
        val materialName = expectIdentifier(inputFile)
        if (materialName !in materials)
            throw GrammarError(inputFile.location, "unknown material $materialName")

        expectSymbol(inputFile, ")")

        return CSGUnion(shape1,shape2,transformation, materials[materialName]!! )
    }
    fun parseCSGDifference(inputFile: InStream):CSGDifference{
        expectSymbol(inputFile,"(")
        val shape1=expectShape(inputFile)
        expectSymbol(inputFile,",")
        val shape2=expectShape(inputFile)
        expectSymbol(inputFile,",")
        val transformation=parseTransformation(inputFile)
        expectSymbol(inputFile,",")
        val materialName = expectIdentifier(inputFile)
        if (materialName !in materials)
            throw GrammarError(inputFile.location, "unknown material $materialName")

        expectSymbol(inputFile, ")")

        return CSGDifference(shape1,shape2,transformation, materials[materialName]!! )
    }

    fun parseCSGIntersection(inputFile: InStream):CSGIntersection{
        expectSymbol(inputFile,"(")
        val shape1=expectShape(inputFile)
        expectSymbol(inputFile,",")
        val shape2=expectShape(inputFile)
        expectSymbol(inputFile,",")
        val transformation=parseTransformation(inputFile)
        expectSymbol(inputFile,",")
        val materialName = expectIdentifier(inputFile)
        if (materialName !in materials)
            throw GrammarError(inputFile.location, "unknown material $materialName")

        expectSymbol(inputFile, ")")

        return CSGIntersection(shape1,shape2,transformation, materials[materialName]!! )
    }

    //da aggiornare
    private val shapeToParse: Map<KeyWordEnum, (InStream) -> Any> = mapOf(
        KeyWordEnum.SPHERE to ::parseSphere,
        KeyWordEnum.PLANE to ::parsePlane,
        KeyWordEnum.CSGUNION to:: parseCSGUnion,
        KeyWordEnum.CSGDIFFERENCE to:: parseCSGDifference,
        KeyWordEnum.CSGINTERSECTION to:: parseCSGIntersection
        
    )

}







}


