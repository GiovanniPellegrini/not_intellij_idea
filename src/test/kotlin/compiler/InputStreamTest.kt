package compiler

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream


class InputStreamTest{

    @Test
    fun testInputFile(){
        val stream = InStream(ByteArrayInputStream("abc   \nd\nef".toByteArray()))
        assertEquals(stream.location.lineNumber,1)
        assertEquals(stream.location.columnNumber, 1)

        assertEquals('a', stream.readChar())
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber, 2)

        stream.unreadChar('A')
        assertEquals(stream.location.lineNumber,1)
        assertEquals(stream.location.columnNumber , 1)

        assertEquals(stream.readChar(), 'A')
        assertEquals(stream.location.lineNumber,1)
        assertEquals(stream.location.columnNumber,2)

        assertEquals(stream.readChar(), 'b')
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber, 3)

        assertEquals(stream.readChar(), 'c')
        assertEquals(stream.location.lineNumber, 1)
        assertEquals(stream.location.columnNumber ,4)

        stream.skipWhiteSpace()

        assertEquals(stream.readChar(), 'd')
        assertEquals(stream.location.lineNumber ,2)
        assertEquals(stream.location.columnNumber,2)

        assertEquals(stream.readChar(),'\n')
        assertEquals(stream.location.lineNumber, 3)
        assertEquals(stream.location.columnNumber,1)

        assertEquals(stream.readChar(), 'e')
        assertEquals(stream.location.lineNumber, 3)
        assertEquals(stream.location.columnNumber, 2)

        assertEquals(stream.readChar(),'f')
        assertEquals(stream.location.lineNumber,3)
        assertEquals(stream.location.columnNumber,3)

        //end of the file
        assertEquals('\u0000', stream.readChar())
    }

    @Test
    fun testEndOfFile(){
        val stream = InStream(ByteArrayInputStream("a\u0000".toByteArray()))
        assertEquals('a', stream.readChar())
        assertEquals('\u0000', stream.readChar())
        assertEquals('\u0000', stream.readChar())
        stream.readChar()
        assertEquals('\u0000', stream.readChar())

    }

    @Test
    fun readTokenTest(){
        val file=ByteArrayInputStream("""
        % This is a comment
        % This is another comment
        new material skyMaterial(
            diffuse(image("my file.pfm")),
            <5.0, 500.0, 300.0 >
        ) % Comment at the end of the line
        """.toByteArray())

        val stream=InStream(stream = file)

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
        if (token is LiteralStringToken) assert(token.string=="my file.pfm")

        token = stream.readToken()
        assert(token is SymbolToken)
        if (token is SymbolToken) assert(token.char == ')')

        token = stream.readToken()
        assert(token is SymbolToken)
        if (token is SymbolToken) assert(token.char == ')')

        token=stream.readToken()
        assert(token is SymbolToken)
        if(token is SymbolToken) assert(token.char==',')

        token=stream.readToken()
        assert(token is SymbolToken)
        if(token is SymbolToken) assert(token.char=='<')

        token=stream.readToken()
        assert(token is LiteralNumberToken)
        if(token is LiteralNumberToken) assert(token.number==5.0f)

       for(i in 1..7 ) {
            token=stream.readToken()
       }
        assertTrue(token is StopToken)
    }

    @Test
    fun TestParser(){
        val stream = ByteArrayInputStream("""
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
        """.toByteArray())

        //TODO("parse_scene function not implemented")

    }
}


