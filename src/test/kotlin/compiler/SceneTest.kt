package compiler

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

class SceneTest{
    @Test
    fun variablesTest(){
        val a = "a"
        val map = mutableMapOf<String, Float>(a to 3f)
        val scene = Scene(overriddenVariables = map)
        //assertEquals(3f, scene.overriddenVariables[a])

        val stream = ByteArrayInputStream(
            """
            
            material s_material(
            diffuse(uniform(<0,0,0>)),
            uniform(<0,0,0>)
            )
            
            sphere s(rotation_x(a), s_material) 
            
            camera(perspective, identity, 1, 1)
            """.toByteArray()
        )
        val streamReader = InputStreamReader(stream)
        scene.parseScene(InStream(streamReader))
    }
}