import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SphereTest {
    @Test
    fun testHit() {
        val sphere = Sphere()

        val ray1 = Ray(origin = Point(0f, 0f, 2f), dir = -Vector(0f, 0f, 1f))
        val intersection1 = sphere.rayIntersection(ray1)
        assertNotNull(intersection1) { "Error: intersection1 is null" }

        if (intersection1 is HitRecord) {
            assert(
                HitRecord(
                    worldPoint = Point(0f, 0f, 1f),
                    normal = Normal(0f, 0f, 1f),
                    surfacePoint = Vec2d(0f, 0f),
                    t = 1f,
                    ray = ray1,
                    shape = sphere
                ).isClose(intersection1)
            )
        }

        val ray2 = Ray(origin = Point(3f, 0f, 0f), dir = -Vector(1f, 0f, 0f))
        val intersection2 = sphere.rayIntersection(ray2)
        assertNotNull(intersection1) { "Error: intersection2 is null" }

        if (intersection2 is HitRecord) {
            assert(
                HitRecord(
                    worldPoint = Point(1f, 0f, 0f),
                    normal = Normal(1f, 0f, 0f),
                    surfacePoint = Vec2d(0f, 0.5f),
                    t = 2f,
                    ray = ray2,
                    shape = sphere
                ).isClose(intersection2)
            )
        }

        //verify that doesn't intersect
        assertNull(sphere.rayIntersection(Ray(origin = Point(0f, 10f, 2f), dir = -Vector(0f, 0f, 1f))))
    }

    @Test
    fun testInnerHit() {
        val sphere = Sphere()

        val ray = Ray(origin = Point(0f, 0f, 0f), dir = Vector(1f, 0f, 0f))
        val intersection = sphere.rayIntersection(ray)
        assertNotNull(intersection)
        if (intersection is HitRecord) {
            assert(
                HitRecord(
                    worldPoint = Point(1.0f, 0.0f, 0.0f),
                    normal = Normal(-1.0f, 0.0f, 0.0f),
                    surfacePoint = Vec2d(0.0f, 0.5f),
                    t = 1f,
                    ray = ray,
                    shape = sphere
                ).isClose(intersection)
            )
        }
    }

    @Test
    fun testTransformation() {
        val sphere = Sphere(transformation = Translation(Vector(10f, 0f, 0f)))

        val ray1 = Ray(origin = Point(10f, 0f, 2f), dir = -Vector(0f, 0f, 1f))
        val intersection1 = sphere.rayIntersection(ray1)
        assertNotNull(intersection1)
        if (intersection1 is HitRecord) {
            assert(
                HitRecord(
                    worldPoint = Point(10.0f, 0.0f, 1.0f),
                    normal = Normal(0.0f, 0.0f, 1.0f),
                    surfacePoint = Vec2d(0.0f, 0.0f),
                    t = 1.0f,
                    ray = ray1,
                    shape = sphere
                ).isClose(intersection1)
            )
        }

        val ray2 = Ray(origin = Point(13f, 0f, 0f), dir = -Vector(1f, 0f, 0f))
        val intersection2 = sphere.rayIntersection(ray2)
        assertNotNull(intersection2)
        if (intersection2 is HitRecord) {
            assert(
                HitRecord(
                    worldPoint = Point(11.0f, 0.0f, 0.0f),
                    normal = Normal(1.0f, 0.0f, 0.0f),
                    surfacePoint = Vec2d(0.0f, 0.5f),
                    t = 2.0f,
                    ray = ray2,
                    shape = sphere
                ).isClose(intersection2)
            )
        }

        //Check if the sphere failed to move by trying to hit the untransformed shape
        assertNull(sphere.rayIntersection(Ray(origin = Point(0f, 0f, 2f), dir = -Vector(0f, 0f, 1f))))

        //Check if the *inverse* transformation was wrongly applied
        assertNull(sphere.rayIntersection(Ray(origin = Point(-10f, 0f, 0f), dir = -Vector(0f, 0f, 1f))))

    }

    @Test
    fun testNormal() {
        val sphere = Sphere(transformation = scalingTransformation(Vector(2.0f, 1.0f, 1.0f)))
        val ray = Ray(origin = Point(1.0f, 1.0f, 0.0f), dir = Vector(-1.0f, -1.0f))
        val intersection = sphere.rayIntersection(ray)
        if (intersection is HitRecord) {
            intersection.normal.normalize()
            val normal = Normal(1.0f, 4.0f, 0.0f).normalize()
            assert(intersection.normal.isClose(normal))
        }
    }

    @Test
    fun testNormalDirection() {
        val sphere = Sphere(transformation = scalingTransformation(Vector(-1f, -1f, -1f)))
        val ray = Ray(origin = Point(0.0f, 2.0f, 0.0f), dir = Vector(0.0f, -1.0f, 0f))
        val intersection = sphere.rayIntersection(ray)
        if (intersection is HitRecord) {
            assert(
                intersection.normal.normalize().isClose(Normal(0.0f, 1.0f, 0.0f).normalize())
            ) { "Error: normal is not correct" }
        }

    }

    @Test
    fun testUVCoordinates() {
        val sphere = Sphere()
        val ray1 = Ray(Point(2f, 0f, 0f), Vector(-1f, 0f, 0f))
        assertNotNull(sphere.rayIntersection(ray1))
        assert(sphere.rayIntersection(ray1)!!.surfacePoint.isClose(Vec2d(0.0f, 0.5f)))

        val ray2 = Ray(Point(0f, 2f, 0f), Vector(0f, -1f, 0f))
        assertNotNull(sphere.rayIntersection(ray2))
        assert(sphere.rayIntersection(ray2)!!.surfacePoint.isClose(Vec2d(0.25f, 0.5f)))

        val ray3 = Ray(Point(-2f, 0f, 0f), Vector(1f, 0f, 0f))
        assertNotNull(sphere.rayIntersection(ray3))
        assert(sphere.rayIntersection(ray3)!!.surfacePoint.isClose(Vec2d(0.5f, 0.5f)))

        val ray4 = Ray(Point(0f, -2f, 0f), Vector(0f, 1f, 0f))
        assertNotNull(sphere.rayIntersection(ray4))
        assert(sphere.rayIntersection(ray4)!!.surfacePoint.isClose(Vec2d(0.75f, 0.5f)))

        val ray5 = Ray(Point(2f, 0f, 0.5f), Vector(-1f, 0f, 0f))
        assertNotNull(sphere.rayIntersection(ray5))
        assert(sphere.rayIntersection(ray5)!!.surfacePoint.isClose(Vec2d(0f, 1 / 3f)))

        val ray6 = Ray(Point(2f, 0f, -0.5f), Vector(-1f, 0f, 0f))
        assertNotNull(sphere.rayIntersection(ray6))
        assert(sphere.rayIntersection(ray6)!!.surfacePoint.isClose(Vec2d(0f, 2 / 3f)))
    }

    @Test
    fun sphereListIntersection() {
        val sphere1 = Sphere()
        val ray = Ray(Point(0f, 0f, 2f), Vector(0f, 0f, -1f))


        val hits = sphere1.rayIntersectionList(ray)
        assertNotNull(hits)
        assertNotNull(hits!![0])
        assertNotNull(hits[1])
        val hit1 = HitRecord(
            worldPoint = Point(0f, 0f, 1f),
            normal = Normal(0f, 0f, 1f),
            surfacePoint = Vec2d(0f, 0f),
            t = 1f,
            ray = ray,
            shape = sphere1,
        )
        assert(hits[0].isClose(hit1))


        val hit2 = HitRecord(
            worldPoint = Point(0f, 0f, -1f),
            normal = Normal(0f, 0f, 1f),
            surfacePoint = Vec2d(0f, 1f),
            t = 3f,
            ray = ray,
            shape = sphere1,
        )
        assert(hits[1].isClose((hit2)))
    }

    @Test
    fun pointInternal() {
        val sphere1 = Sphere()

        val point1 = Point(0.5f, 0.4f, 0.7f)
        val point2 = Point(1f, 0f, 0f)
        val point3 = Point(1f, 4f, 6f)

        assert(sphere1.pointInternal(point1))
        assert(sphere1.pointInternal(point2))
        assert(!sphere1.pointInternal(point3))

        val sphere2 = Sphere(Translation(Vector(1f, 0f, 0f)))

        val point4 = Point(1f, 0.5f, 0.6f)
        val point5 = Point(0f, 0f, 0f)
        val point6 = Point(0f, 0f, 1f)

        assert(sphere2.pointInternal(point4))
        assert(sphere2.pointInternal(point5))
        assert(!sphere2.pointInternal(point6))
    }
}