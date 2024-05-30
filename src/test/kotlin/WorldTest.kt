import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WorldTest{
    @Test
    fun testWorld(){
        val world = World()
        val sphere1 = Sphere(transformation = Translation(Vector(1f,0f,0f) * 2f))
        val sphere2 = Sphere(transformation = Translation(Vector(1f,0f,0f) * 8f))
        world.addShape(sphere1)
        world.addShape(sphere2)

        val intersection1 = world.rayIntersection(Ray(origin=Point(0.0f, 0.0f, 0.0f),
                                                      dir = Vector(1f,0f,0f)))
        assertNotNull(intersection1)

        assert(intersection1!!.worldPoint.isClose(Point(1.0f, 0.0f, 0.0f)))

        val intersection2 = world.rayIntersection(Ray(origin=Point(10.0f, 0.0f, 0.0f),
                                                      dir=-Vector(1f,0f,0f)
        ))

        assertNotNull(intersection2)
        assert(intersection2!!.worldPoint.isClose(Point(9.0f, 0.0f, 0.0f)))
    }

    @Test
    fun testQuickRayIntersectionSphere(){
        val world = World()
        val sphere1 = Sphere(transformation = Translation(Vector(1f,0f,0f) * 2f))
        val sphere2 = Sphere(transformation = Translation(Vector(1f,0f,0f) * 8f))
        world.addShape(sphere1)
        world.addShape(sphere2)

        assert(!world.isPointVisible(point = Point(10.0f, 0.0f, 0.0f),
                                    obsPos = Point(0.0f, 0.0f, 0.0f)))
        assert(!world.isPointVisible(point = Point(5.0f, 0.0f, 0.0f),
                                    obsPos = Point(0.0f, 0.0f, 0.0f)))
        assert(world.isPointVisible(point = Point(5.0f, 0.0f, 0.0f),
                                    obsPos = Point(4.0f, 0.0f, 0.0f)))
        assert(world.isPointVisible(point = Point(0.5f, 0.0f, 0.0f),
                                    obsPos = Point(0f, 0.0f, 0.0f)))
        assert(world.isPointVisible(point = Point(0.0f,10.0f, 0.0f),
                                    obsPos = Point(0.0f, 0.0f, 0.0f)))
        assert(world.isPointVisible(point = Point(0.0f, 0.0f, 10.0f),
                                    obsPos = Point(0.0f, 0.0f, 0.0f)))
    }

    @Test
    fun testQuickRayIntersectionTriangle(){
        val world = World()
        val triangle = Triangle(a=Point(0f,0f,0f),
                                b=Point(0f,1f,0f),
                                c=Point(1f,0f,0f))
        world.addShape(triangle)

        assert(!world.isPointVisible(point = Point(0.5f, 0.5f, 1.0f),
                                    obsPos = Point(0.5f, 0.5f, -1.0f)))
        assert(world.isPointVisible(point = Point(0.5f, 0.5f, 2.0f),
                                    obsPos = Point(-1.5f, 0.0f, -2.0f)))
    }

    @Test
    fun testQuickRayIntersectionPlane(){
        val world = World()
        val plane1 = Plane()
        val plane2 = Plane(transformation = Rotation(Vector(0f, 1f, 0f), 90f))
        world.addShape(plane1)
        world.addShape(plane2)

        assert(!world.isPointVisible(point = Point(-0.5f, 0.5f, 1.0f),
                                     obsPos = Point(-0.5f, -0.5f, -3.0f)))

        assert(world.isPointVisible(point = Point(-0.5f, -0.5f, 2.0f),
                                    obsPos = Point(-0.5f, -0.5f, 1.0f)))

    }

}