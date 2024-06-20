import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WorldTest {
    @Test
    fun testWorld() {
        val world = World()
        val sphere1 = Sphere(transformation = Translation(Vector(1f,0f,0f) * 2f))
        val sphere2 = Sphere(transformation = Translation(Vector(1f,0f,0f) * 8f))
        world.addShape(sphere1)
        world.addShape(sphere2)

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

    @Test
    fun testQuickRayIntersectionBox(){
        val world = World()
        val box = Box(Pmax = Point(1f,1f,2f), Pmin = Point(-1f,-1f,-2f))
        world.addShape(box)

        assert(!world.isPointVisible(point = Point(0f, 0f, 1f),
                                     obsPos = Point(0f, 0f, -1f)))

        assert(!world.isPointVisible(point = Point(0f, 0f, 2f),
                                    obsPos = Point(0f, 0f, 0f)))

        assert(world.isPointVisible(point = Point(0f, 0f, 2f),
                                    obsPos = Point(0f, 3f, 0f)))

    }

    @Test
    fun testQuickRayIntersectionUnion(){
        val world = World()
        val sphere1 = Sphere(transformation = Translation(Vector(1f,0f,0f) * 0.5f))
        val sphere2 = Sphere(transformation = Translation(Vector(-1f,0f,0f) * 0.5f))
        val union = CSGUnion(sphere1, sphere2)

        world.addShape(union)

        assert(!world.isPointVisible(point = Point(3f, 0f, 0f),
                                     obsPos = Point(-2f, 0f, 0f)))

        assert(!world.isPointVisible(point = Point(-1.3f, 0f, 2f),
                                    obsPos = Point(-1.3f, 0f, -2f)))

        assert(world.isPointVisible(point = Point(0f, 0f, 4f),
                                    obsPos = Point(2f, 0f, 0f)))
    }

    @Test
    fun testQuickRayIntersectionDifference(){
        val world = World()
        val sphere1 = Sphere()
        val sphere2 = Sphere(transformation = Translation(Vector(-1f,0f,0f) * 0.5f))
        val difference = CSGDifference(sphere1, sphere2)

        world.addShape(difference)

        assert(!world.isPointVisible(point = Point(3f, 0f, 0f),
            obsPos = Point(-2f, 0f, 0f)))

        assert(world.isPointVisible(point = Point(-0.1f, 0f, 0f),
            obsPos = Point(2f, 0.3f, 0f)))

        assert(world.isPointVisible(point = Point(-0.6f, 0f, 1f),
                                    obsPos = Point(-0.6f, 0f, -1f)))
    }

    @Test
    fun testQuickRayIntersectionInt(){
        val world = World()
        val sphere1 = Sphere(transformation = Translation(Vector(0f,-1f,0f) * 0.5f))
        val sphere2 = Sphere(transformation = Translation(Vector(0f,1f,0f) * 0.5f))
        val difference = CSGIntersection(sphere1, sphere2)

        world.addShape(difference)

        assert(!world.isPointVisible(point = Point(3f, 0f, 0f),
            obsPos = Point(-2f, 0f, 0f)))

        assert(world.isPointVisible(point = Point(0f, 0f, 2f),
            obsPos = Point(-2.2f, 0f, 0f)))

        //this assertion fails
        assert(world.isPointVisible(point = Point(0f, -0.7f, 1f),
                                    obsPos = Point(0f, -0.7f, -1f)))
    }

}