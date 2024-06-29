package compiler

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.InputStreamReader
import Ray
import Point
import Sphere
import Vector

class SceneTest {
    @Test
    fun variablesTest() {
        val a = "a"
        val map = mutableMapOf<String, Float>(a to 10f, "b" to 4f, "c" to 5f)
        val scene = Scene(overriddenVariables = map, floatVariables = map)
        assertEquals(10f, scene.overriddenVariables[a])

        val stream = ByteArrayInputStream(
            """
            float a(0.1)
            float pippo(0.2)
            material s_material(
            diffuse(uniform(<0,0,0>)),
            uniform(<0,0,0>)
            )
            
            sphere s(scaling([a,a,a]), s_material) 
            
            camera(perspective, identity, 1, 1)
            """.toByteArray()
        )
        val streamReader = InputStreamReader(stream)
        scene.parseScene(InStream(streamReader))

        assertNotEquals(0.1f, scene.floatVariables["a"])

        val ray = Ray(origin = Point(-3f, 0f, 5f), dir = Vector(1f, 0f, 0f))
        assertNotNull(scene.world.shapes[0].rayIntersection(ray))
    }
}