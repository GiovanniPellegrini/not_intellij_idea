package compiler

import CheckeredPigment
import DiffusionBRDF
import SpecularBRDF
import UniformPigment
import Plane
import Triangle
import PerspectiveCamera
import Transformation
import Sphere
import Translation
import Box
import Vector
import Vec2d
import Color
import Rotation
import TriangleMesh
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import kotlin.test.assertFailsWith


class InputStreamTest {

    @Test
    fun testInputFile() {
        val stream = InStream(InputStreamReader(ByteArrayInputStream("abc   \nd\nef".toByteArray())))
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber, 1)

        assertEquals('a', stream.readChar())
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber, 2)

        stream.unreadChar('A')
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber, 1)

        assertEquals(stream.readChar(), 'A')
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber, 2)

        assertEquals(stream.readChar(), 'b')
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber, 3)

        assertEquals(stream.readChar(), 'c')
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber, 4)

        stream.skipWhiteSpace()

        assertEquals(stream.readChar(), 'd')
        assertEquals(stream.location.lineNumber, 2)
        assertEquals(stream.location.columnNumber, 2)

        assertEquals(stream.readChar(), '\n')
        assertEquals(stream.location.lineNumber, 3)
        assertEquals(stream.location.columnNumber, 1)

        assertEquals(stream.readChar(), 'e')
        assertEquals(stream.location.lineNumber, 3)
        assertEquals(stream.location.columnNumber, 2)

        assertEquals(stream.readChar(), 'f')
        assertEquals(stream.location.lineNumber, 3)
        assertEquals(stream.location.columnNumber, 3)

        //end of the file
        assertEquals('\u0000', stream.readChar())
    }

    @Test
    fun testEndOfFile() {
        val stream = InStream(InputStreamReader(ByteArrayInputStream("a\u0000".toByteArray())))
        assertEquals('a', stream.readChar())
        assertEquals('\u0000', stream.readChar())
        assertEquals('\u0000', stream.readChar())
        stream.readChar()
        assertEquals('\u0000', stream.readChar())

    }

    @Test
    fun readTokenTest() {
        val byteArrayStream = ByteArrayInputStream(
            """
        % This is a comment
        % This is another comment
        new material skyMaterial(
            diffuse(image("my file.pfm")),
            <5.0, 500.0, 300.0 >
        ) % Comment at the end of the line
        """.toByteArray()
        )
        val streamReader = InputStreamReader(byteArrayStream)
        val stream = InStream(streamReader)

        var token = stream.readToken()
        assert(token is KeyWordToken)
        if (token is KeyWordToken) assert(token.keywordEnum == KeyWordEnum.NEW)

        token = stream.readToken()
        assert(token is KeyWordToken)
        if (token is KeyWordToken) assert(token.keywordEnum == KeyWordEnum.MATERIAL)

        token = stream.readToken()
        assert(token is IdentifierToken)
        if (token is IdentifierToken) assert(token.identifier == "skyMaterial")

        token = stream.readToken()
        assert(token is SymbolToken)
        if (token is SymbolToken) assert(token.char == '(')

        token = stream.readToken()
        assert(token is KeyWordToken)
        if (token is KeyWordToken) assert(token.keywordEnum == KeyWordEnum.DIFFUSE)

        token = stream.readToken()
        assert(token is SymbolToken)
        if (token is SymbolToken) assert(token.char == '(')

        token = stream.readToken()
        assert(token is KeyWordToken)
        if (token is KeyWordToken) assert(token.keywordEnum == KeyWordEnum.IMAGE)

        token = stream.readToken()
        assert(token is SymbolToken)
        if (token is SymbolToken) assert(token.char == '(')

        token = stream.readToken()
        assert(token is LiteralStringToken)
        if (token is LiteralStringToken) assert(token.string == "my file.pfm")

        token = stream.readToken()
        assert(token is SymbolToken)
        if (token is SymbolToken) assert(token.char == ')')

        token = stream.readToken()
        assert(token is SymbolToken)
        if (token is SymbolToken) assert(token.char == ')')

        token = stream.readToken()
        assert(token is SymbolToken)
        if (token is SymbolToken) assert(token.char == ',')

        token = stream.readToken()
        assert(token is SymbolToken)
        if (token is SymbolToken) assert(token.char == '<')

        token = stream.readToken()
        assert(token is LiteralNumberToken)
        if (token is LiteralNumberToken) assert(token.number == 5.0f)

        for (i in 1..7) {
            token = stream.readToken()
        }
        assertTrue(token is StopToken)
    }

    @Test
    fun testParser() {
        val byteArrayStream = ByteArrayInputStream(
            """
        float clock(150)
    
        material sky_material(
            diffuse(uniform(<0, 0, 0>)),
            uniform(<0.7, 0.5, 1>)
        )
    
        % Here is a comment
    
        material ground_material(
            diffuse(checkered(<0.3, 0.5, 0.1>,
                              <0.1, 0.2, 0.5>, 4)),
            uniform(<0, 0, 0>)
        )
    
        material sphere_material(
            specular(uniform(<0.5, 0.5, 0.5>)),
            uniform(<0, 0, 0>)
        )
    
        plane (sky_material, translation(<0, 0, 100>) * rotation_y(clock))
        plane (ground_material, identity)
    
        sphere(sphere_material, translation(<0, 0, 1>))
    
        camera(perspective, rotation_z(30) * translation(<-4, 0, 1>), 1.0, 2.0)
        """.toByteArray()
        )
        val streamReader = InputStreamReader(byteArrayStream)
        val scene = Scene()
        scene.parseScene(InStream(streamReader))

        assertEquals(scene.floatVariables.size, 1)
        assert("clock" in scene.floatVariables.keys)
        assert(scene.floatVariables["clock"] == 150f)

        assertEquals(scene.materials.size, 3)
        assert("sky_material" in scene.materials.keys)
        assert("ground_material" in scene.materials.keys)
        assert("sphere_material" in scene.materials.keys)

        val sphereMaterial = scene.materials["sphere_material"]
        val skyMaterial = scene.materials["sky_material"]
        val groundMaterial = scene.materials["ground_material"]

        assert(skyMaterial?.brdf is DiffusionBRDF)
        assert(skyMaterial?.brdf?.p is UniformPigment)
        assert(skyMaterial?.brdf?.p?.getColor(Vec2d(0f, 0f)) == Color())

        assert(groundMaterial?.brdf is DiffusionBRDF)
        assert(groundMaterial?.brdf?.p is CheckeredPigment)
        assert(groundMaterial?.brdf?.p?.getColor(Vec2d(2.0f, 2.0f)) == Color(0.3f, 0.5f, 0.1f))
        assert(groundMaterial?.brdf?.p?.getColor(Vec2d(2.0f, 0.9f)) == Color(0.1f, 0.2f, 0.5f))

        assert(sphereMaterial?.brdf is SpecularBRDF)
        assert(sphereMaterial?.brdf?.p is UniformPigment)
        assert(sphereMaterial?.brdf?.p?.getColor(Vec2d(0.0f, 0.0f)) == Color(0.5f, 0.5f, 0.5f))

        assert(skyMaterial?.emittedRad is UniformPigment)
        assert(skyMaterial?.emittedRad?.getColor(Vec2d(0.0f, 0.0f)) == Color(0.7f, 0.5f, 1.0f))
        assert(groundMaterial?.emittedRad is UniformPigment)
        assert(groundMaterial?.emittedRad?.getColor(Vec2d(0.0f, 0.0f)) == Color(0.0f, 0.0f, 0.0f))
        assert(sphereMaterial?.emittedRad is UniformPigment)
        assert(sphereMaterial?.emittedRad?.getColor(Vec2d(0.0f, 0.0f)) == Color(0.0f, 0.0f, 0.0f))

        assert(scene.world.shapes.size == 3)
        assert(scene.world.shapes[0] is Plane)
        assert(
            scene.world.shapes[0].transformation.isClose(
                Translation(Vector(0.0f, 0.0f, 100.0f)) * Rotation(Vector(0f, 1f, 0f), 150.0f)
            )
        )
        assert(scene.world.shapes[1] is Plane)
        assert(scene.world.shapes[1].transformation.isClose(Transformation()))
        assert(scene.world.shapes[2] is Sphere)
        assert(scene.world.shapes[2].transformation.isClose(Translation(Vector(0.0f, 0.0f, 1.0f))))

        assert(scene.camera is PerspectiveCamera)

    }

    @Test
    fun parserUndefMaterialTest() {
        val scene = Scene()
        val byteArrayStream = ByteArrayInputStream(
            """
        plane(this_material_does_not_exist, identity)
        """.toByteArray()
        )
        val streamReader = InputStreamReader(byteArrayStream)
        assertFailsWith<GrammarError> {
            scene.parseScene(InStream(streamReader))
        }
    }

    @Test
    fun parseDoubleCam() {
        val scene = Scene()
        val byteArrayStream = ByteArrayInputStream(
            """
        camera(perspective, rotation_z(30) * translation([-4, 0, 1]), 1.0, 1.0)
        camera(orthogonal, identity, 1.0, 1.0)
        """.toByteArray()
        )
        val streamReader = InputStreamReader(byteArrayStream)
        assertFailsWith<GrammarError> {
            scene.parseScene(InStream(streamReader))
        }

    }

    @Test
    fun parseTrianglesTest() {
        val scene = Scene()
        val byteArrayStream = ByteArrayInputStream(
            """
        material triangle_material(
            specular(uniform(<0.5, 0.5, 0.5>)),
            uniform(<0, 0, 0>)
        )
        
        material triangle_mesh_material(
            diffuse(uniform(<0.7, 0.8, 0.2>)),
            uniform(<0.4, 0, 0>)
        )
                Triangle((1.0,1.0,1.0), (2.0,2.0,2.0), (3.0,3.0,3.0), rotation_x(23), triangle_material)
                TriangleMesh(((1.0,1.0,1.0), (2.0,2.0,2.0), (3.0,3.0,3.0),(4.0,1.0,1.0), (5.0,2.0,2.0), (6.0,3.0,3.0)),
                  ((1,2,3), (4,5,6), (2,3,6), (1,3,5)), translation(<-4, 0, 1>), triangle_mesh_material)
            """.toByteArray()
        )
        val streamReader = InputStreamReader(byteArrayStream)
        scene.parseScene(InStream(streamReader))
        assert(scene.world.shapes[0] is Triangle)
        assert(
            scene.world.shapes[0].transformation.isClose(
                Rotation(Vector(1f, 0f, 0f), 23.0f)
            )
        )
        assert(scene.world.shapes[1] is TriangleMesh)
        assert(scene.world.shapes[1].material.brdf is DiffusionBRDF)
    }

    @Test
    fun parseBoxTest(){
        val scene = Scene()
        val byteArrayStream = ByteArrayInputStream(
            """
        material box_material(
            specular(uniform(<0.5, 0.5, 0.5>)),
            uniform(<0, 0, 0>)
        )
        
        Box((1.0,1.0,1.0), (-2.0,-2.0,-2.0), rotation_z(123), box_material)
            """.toByteArray()
        )
        val streamReader = InputStreamReader(byteArrayStream)
        scene.parseScene(InStream(streamReader))
        assert(scene.world.shapes[0] is Box)
        assert(scene.world.shapes[0].transformation == Rotation(Vector(0f,0f,1f), 123f))
        assert(scene.world.shapes[0].material.brdf is SpecularBRDF)
    }
}


