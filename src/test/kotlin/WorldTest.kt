import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WorldTest {
    @Test
    fun testWorld() {
        val world = World()
        val sphere1 = Sphere(transformation = Translation(Vector(1f, 0f, 0f) * 2f))
        val sphere2 = Sphere(transformation = Translation(Vector(1f, 0f, 0f) * 8f))
        world.add(sphere1)
        world.add(sphere2)

        val intersection1 = world.rayIntersection(
            Ray(
                origin = Point(0.0f, 0.0f, 0.0f),
                dir = Vector(1f, 0f, 0f)
            )
        )
        assertNotNull(intersection1)

        assert(intersection1!!.worldPoint.isClose(Point(1.0f, 0.0f, 0.0f)))

        val intersection2 = world.rayIntersection(
            Ray(
                origin = Point(10.0f, 0.0f, 0.0f),
                dir = -Vector(1f, 0f, 0f)
            )
        )

        assertNotNull(intersection2)
        assert(intersection2!!.worldPoint.isClose(Point(9.0f, 0.0f, 0.0f)))
    }

}