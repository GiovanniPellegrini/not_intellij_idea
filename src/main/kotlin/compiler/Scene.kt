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
import Box
import Color
import ImagePigment
import OrthogonalCamera
import PerspectiveCamera
import Pigment
import Transformation
import Point
import DiffusionBRDF
import SpecularBRDF
import Triangle
import TriangleMesh
import scalingTransformation
import UniformPigment
import readPfmImage
import java.io.FileInputStream
import Sphere
import Plane
import PointLight
import Shape

/**
 * Scene class, contains all variables included in a scene file
 * @param materials: Dict of "materialName" -> `Material`
 * @param world: Container of all shapes
 * @param camera: Observer camera
 * @param floatVariables: floating point variable declared in the file
 * @param overriddenVariables: overridden floating point variable declared in the file
 */
class Scene(
    val materials: MutableMap<String, Material> = mutableMapOf(),
    val world: World = World(),
    var camera: Camera? = null,
    var floatVariables: MutableMap<String, Float> = mutableMapOf(),
    private var overriddenVariables: MutableSet<String> = mutableSetOf(),
    var shapes: MutableMap<String, Shape> = mutableMapOf()
) {
    /**
     * Reads a token and checks if it is a symbol
     */
    private fun expectSymbol(inputFile: InStream, symbol: String) {
        val token = inputFile.readToken()
        if (token !is SymbolToken) {
            throw GrammarError(token.location, "Expected symbol $symbol, but got $token")
        }
    }

    /**
     * Reads a token and checks if it is a keyword,
     * @return the keyword that was read as a KeyWordEnum
     */
    private fun expectKeyWords(inputFile: InStream, keywords: List<KeyWordEnum>): KeyWordEnum {
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
    private fun expectNumber(inputFile: InStream): Float {
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
    private fun expectString(inputFile: InStream): String {
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
    private fun expectIdentifier(inputFile: InStream): String {
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


    // Parsing Functions for all the possible objects in the scene

    private fun parseVector(inStream: InStream): Vector {
        expectSymbol(inStream, "<")
        val x = expectNumber(inStream)
        expectSymbol(inStream, ",")
        val y = expectNumber(inStream)
        expectSymbol(inStream, ",")
        val z = expectNumber(inStream)
        expectSymbol(inStream, ">")

        return Vector(x, y, z)
    }

    private fun parseColor(inStream: InStream): Color {
        expectSymbol(inStream, "<")
        val r = expectNumber(inStream)
        expectSymbol(inStream, ",")
        val g = expectNumber(inStream)
        expectSymbol(inStream, ",")
        val b = expectNumber(inStream)
        expectSymbol(inStream, ">")

        return Color(r, g, b)
    }

    private fun parsePoint(inputStream: InStream): Point {
        expectSymbol(inputStream, "(")
        val x = expectNumber(inputStream)
        expectSymbol(inputStream, ",")
        val y = expectNumber(inputStream)
        expectSymbol(inputStream, ",")
        val z = expectNumber(inputStream)
        expectSymbol(inputStream, ")")

        return Point(x, y, z)
    }

    private fun parseListOfPoints(inputStream: InStream): MutableList<Point> {
        val points = mutableListOf<Point>()
        while (true) {
            points.add(parsePoint(inputStream)) // Parse each point

            val nextToken = inputStream.readToken()
            if (nextToken is SymbolToken && nextToken.char == ')') {
                break // End of the list
            } else if (nextToken is SymbolToken && nextToken.char == ',') {
                continue // Next point
            } else {
                throw GrammarError(nextToken.location, "Unexpected token $nextToken in list of points")
            }
        }

        return points
    }

    private fun parseListOfInt(inputStream: InStream): MutableList<Int> {
        val numbers = mutableListOf<Int>()

        expectSymbol(inputStream, "(") // Lists starts with "["

        while (true) {
            numbers.add(expectNumber(inputStream).toInt())
            val nextToken = inputStream.readToken()
            if (nextToken is SymbolToken && nextToken.char == ')') {
                break // End of the list
            } else if (nextToken is SymbolToken && nextToken.char == ',') {
                continue // Next point
            } else {
                throw GrammarError(nextToken.location, "Unexpected token $nextToken in list of points")
            }
        }

        return numbers
    }

    private fun parseListOfList(inputStream: InStream): MutableList<MutableList<Int>> {
        val listOfList: MutableList<MutableList<Int>> = mutableListOf(mutableListOf())
        expectSymbol(inputStream, "(")
        while (true) {
            listOfList.add(parseListOfInt(inputStream))
            val nextToken = inputStream.readToken()
            if (nextToken is SymbolToken && nextToken.char == ')') {
                break // End of the list
            } else if (nextToken is SymbolToken && nextToken.char == ',') {
                continue // Next point
            } else {
                throw GrammarError(nextToken.location, "Unexpected token $nextToken in list of points")
            }
        }
        return listOfList
    }

    private fun parsePigment(inputStream: InStream): Pigment {
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

    private fun parseBrdf(inputStream: InStream): BRDF {
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

    private fun parseMaterial(inputStream: InStream): Map<String, Material> {
        val materialName = expectIdentifier(inputStream)
        expectSymbol(inputStream, "(")
        val brdf = parseBrdf(inputStream)
        expectSymbol(inputStream, ",")
        val emittedRad = parsePigment(inputStream)
        expectSymbol(inputStream, ")")

        return mapOf(materialName to Material(brdf, emittedRad))
    }

    private fun parseSphere(inStream: InStream): Map<String, Shape> {
        val shapeName = expectIdentifier(inStream)
        expectSymbol(inStream, "(")
        val transformation = parseTransformation(inStream)
        expectSymbol(inStream, ",")
        val materialName = expectIdentifier(inStream)
        if (materialName !in materials.keys) {
            throw GrammarError(inStream.location, "unknown material $materialName")
        }
        expectSymbol(inStream, ")")

        return mapOf(shapeName to Sphere(transformation = transformation, material = materials[materialName]!!))
    }

    private fun parsePlane(inStream: InStream): Map<String, Shape> {
        val shapeName = expectIdentifier(inStream)
        expectSymbol(inStream, "(")
        val transformation = parseTransformation(inStream)
        expectSymbol(inStream, ",")
        val materialName = expectIdentifier(inStream)
        if (materialName !in materials) {
            throw GrammarError(inStream.location, "unknown material $materialName")
        }
        expectSymbol(inStream, ")")

        return mapOf(shapeName to Plane(transformation = transformation, material = materials[materialName]!!))
    }

    // Box defined as box myBox(pMax, pMin, transformation, material)
    private fun parseBox(inputStream: InStream): Map<String, Shape> {
        val shapeName = expectIdentifier(inputStream)
        expectSymbol(inputStream, "(")
        val pMin = parsePoint(inputStream)
        expectSymbol(inputStream, ",")
        val pMax = parsePoint(inputStream)
        expectSymbol(inputStream, ",")
        val transformation = parseTransformation(inputStream)
        expectSymbol(inputStream, ",")
        val materialName = expectIdentifier(inputStream)
        if (materialName !in materials) {
            throw GrammarError(inputStream.location, "unknown material $materialName")
        }
        expectSymbol(inputStream, ")")

        return mapOf(
            shapeName to Box(
                Pmax = pMax,
                Pmin = pMin,
                transformation = transformation,
                material = materials[materialName]!!
            )
        )

    }

    // triangle is declared as triangle triangleName(a,b,c,transformation, material)
    private fun parseTriangle(inputStream: InStream): Map<String, Triangle> {
        val triangleName = expectIdentifier(inputStream)
        expectSymbol(inputStream, "(")
        val a = parsePoint(inputStream)
        expectSymbol(inputStream, ",")
        val b = parsePoint(inputStream)
        expectSymbol(inputStream, ",")
        val c = parsePoint(inputStream)
        expectSymbol(inputStream, ",")
        val transformation = parseTransformation(inputStream)
        expectSymbol(inputStream, ",")
        val materialName = expectIdentifier(inputStream)
        if (materialName !in materials) {
            throw GrammarError(inputStream.location, "unknown material $materialName")
        }
        expectSymbol(inputStream, ")")

        return mapOf(triangleName to Triangle(transformation, a, b, c, materials[materialName]!!))
    }

    private fun parseTriangleMesh(inputStream: InStream): Map<String, TriangleMesh> {
        val triangleMeshName = expectIdentifier(inputStream)
        expectSymbol(inputStream, "(")
        val token = inputStream.readToken()
        var filename: String? = null
        val points: MutableList<Point>?
        var indices: MutableList<MutableList<Int>> = mutableListOf(mutableListOf())
        when (token) {
            is LiteralStringToken -> {
                filename = token.string
                points = null
            }

            is SymbolToken -> {
                points = parseListOfPoints(inputStream)
                expectSymbol(inputStream, ",")
                indices = parseListOfList(inputStream)
            }

            else -> {
                throw GrammarError(token.location, "Unexpected token $token for TriangleMesh")
            }
        }
        expectSymbol(inputStream, ",")
        val transformation = parseTransformation(inputStream)
        expectSymbol(inputStream, ",")
        val materialName = expectIdentifier(inputStream)
        if (materialName !in materials) {
            throw GrammarError(inputStream.location, "unknown material $materialName")
        }
        expectSymbol(inputStream, ")")
        return if (points == null) {
            mapOf(
                triangleMeshName to TriangleMesh(
                    filename = filename!!,
                    transformation = transformation,
                    material = materials[materialName]!!
                )
            )
        } else {
            mapOf(
                triangleMeshName to TriangleMesh(
                    vertices = points,
                    indices = indices,
                    transformation = transformation,
                    material = materials[materialName]!!
                )
            )
        }

    }

    private fun parseTransformation(inputStream: InStream): Transformation {
        var result = Transformation()
        while (true) {
            val transformationKw = expectKeyWords(
                inputStream, listOf(
                    KeyWordEnum.IDENTITY,
                    KeyWordEnum.TRANSLATION,
                    KeyWordEnum.ROTATION_X,
                    KeyWordEnum.ROTATION_Y,
                    KeyWordEnum.ROTATION_Z,
                    KeyWordEnum.SCALING
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

    private fun parseCamera(inputStream: InStream): Camera {
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

            else -> throw GrammarError(inputStream.location, "no clear definition of BRDF")
        }
    }

    private fun parseCSGUnion(inputFile: InStream): Map<String, Shape> {
        val shapeCSGName = expectIdentifier(inputFile)
        expectSymbol(inputFile, "(")
        val shape1 = expectIdentifier(inputFile)
        if (shape1 !in shapes)
            throw GrammarError(inputFile.location, "unknown shape $shape1")
        expectSymbol(inputFile, ",")
        val shape2 = expectIdentifier(inputFile)
        if (shape2 !in shapes)
            throw GrammarError(inputFile.location, "unknown shape $shape2")
        expectSymbol(inputFile, ",")
        val transformation = parseTransformation(inputFile)
        expectSymbol(inputFile, ",")
        val materialName = expectIdentifier(inputFile)
        if (materialName !in materials)
            throw GrammarError(inputFile.location, "unknown material $materialName")

        expectSymbol(inputFile, ")")

        val csgUnion = CSGUnion(shapes[shape1]!!, shapes[shape2]!!, transformation, materials[materialName]!!)
        this.shapes.remove(shape1)
        this.shapes.remove(shape2)

        return mapOf(shapeCSGName to csgUnion)
    }

    private fun parseCSGIntersection(inputFile: InStream): Map<String, Shape> {
        val shapeCSGName = expectIdentifier(inputFile)
        expectSymbol(inputFile, "(")
        val shape1 = expectIdentifier(inputFile)
        if (shape1 !in shapes)
            throw GrammarError(inputFile.location, "unknown shape $shape1")
        expectSymbol(inputFile, ",")
        val shape2 = expectIdentifier(inputFile)
        if (shape2 !in shapes)
            throw GrammarError(inputFile.location, "unknown shape $shape2")
        expectSymbol(inputFile, ",")
        val transformation = parseTransformation(inputFile)
        expectSymbol(inputFile, ",")
        val materialName = expectIdentifier(inputFile)
        if (materialName !in materials)
            throw GrammarError(inputFile.location, "unknown material $materialName")

        expectSymbol(inputFile, ")")

        val csgIntersection =
            CSGIntersection(shapes[shape1]!!, shapes[shape2]!!, transformation, materials[materialName]!!)
        this.shapes.remove(shape1)
        this.shapes.remove(shape2)


        return mapOf(
            shapeCSGName to csgIntersection
        )
    }

    private fun parseCSGDifference(inputFile: InStream): Map<String, Shape> {
        val shapeCSGName = expectIdentifier(inputFile)
        expectSymbol(inputFile, "(")
        val shape1 = expectIdentifier(inputFile)
        if (shape1 !in shapes)
            throw GrammarError(inputFile.location, "unknown shape $shape1")
        expectSymbol(inputFile, ",")
        val shape2 = expectIdentifier(inputFile)
        if (shape2 !in shapes)
            throw GrammarError(inputFile.location, "unknown shape $shape2")
        expectSymbol(inputFile, ",")
        val transformation = parseTransformation(inputFile)
        expectSymbol(inputFile, ",")
        val materialName = expectIdentifier(inputFile)
        if (materialName !in materials)
            throw GrammarError(inputFile.location, "unknown material $materialName")

        expectSymbol(inputFile, ")")

        val csgDifference = CSGDifference(shapes[shape1]!!, shapes[shape2]!!, transformation, materials[materialName]!!)
        this.shapes.remove(shape1)
        this.shapes.remove(shape2)


        return mapOf(
            shapeCSGName to csgDifference
        )
    }

    // PointLight is defined as PointLight(Point, Color, radius: Float)
    private fun parsePointLight(inputStream: InStream): PointLight {
        expectSymbol(inputStream, "(")
        val position = parsePoint(inputStream)
        expectSymbol(inputStream, ",")
        val color = parseColor(inputStream)
        expectSymbol(inputStream, ",")
        val radius = expectNumber(inputStream)
        expectSymbol(inputStream, ")")

        return PointLight(position, color, radius)
    }

    /**
     * Reads a scene description from the input stream and returns a Scene object
     */
    fun parseScene(inputStream: InStream) {
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
                    if (variableName in this.floatVariables && variableName !in this.overriddenVariables) {
                        throw GrammarError(variableLocation, "Variable '$variableName' cannot be redefined")
                    }
                    // Define variable if not already defined before
                    if (variableName !in this.overriddenVariables) {
                        this.floatVariables[variableName] = varValue
                    }


                }

                KeyWordEnum.SPHERE -> {
                    val spheres = parseSphere(inputStream)
                    this.shapes.putAll(spheres)
                }

                KeyWordEnum.PLANE -> {
                    val planes = parsePlane(inputStream)
                    this.shapes.putAll(planes)
                }

                KeyWordEnum.TRIANGLE -> {
                    val triangles = parseTriangle(inputStream)
                    this.shapes.putAll(triangles)
                }

                KeyWordEnum.TRIANGLEMESH -> {
                    val triangleMeshes = parseTriangleMesh(inputStream)
                    this.shapes.putAll(triangleMeshes)
                }

                KeyWordEnum.BOX -> {
                    val boxes = parseBox(inputStream)
                    this.shapes.putAll(boxes)
                }

                KeyWordEnum.CAMERA -> {
                    if (camera != null) {
                        throw GrammarError(what.location, "Camera already defined")
                    }
                    camera = parseCamera(inputStream)
                }

                KeyWordEnum.MATERIAL -> {
                    val materials = parseMaterial(inputStream)
                    this.materials.putAll(materials)
                }

                KeyWordEnum.CSGUNION -> {
                    val csgUnions = parseCSGUnion(inputStream)
                    this.shapes.putAll(csgUnions)
                }

                KeyWordEnum.CSGINTERSECTION -> {
                    val csgIntersections = parseCSGIntersection(inputStream)
                    this.shapes.putAll(csgIntersections)
                }

                KeyWordEnum.CSGDIFFERENCE -> {
                    val csgDifferences = parseCSGDifference(inputStream)
                    this.shapes.putAll(csgDifferences)
                }

                KeyWordEnum.POINTLIGHT -> {
                    val pointLight = parsePointLight(inputStream)
                    this.world.addPointLight(pointLight)
                }

                else -> {
                    throw GrammarError(what.location, "Unexpected token $what")
                }
            }
        }
        for (shape in shapes.values) {
            this.world.addShape(shape)
        }
    }
}


