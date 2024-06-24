import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PlaneTest {
    @Test
    fun planeTest() {
        val plane1 = Plane()
        val ray1 = Ray(Point(0f, 0f, 1f), Vector(0f, 0f, -1f))

        val intersection = plane1.rayIntersection(ray1)
        assertNotNull(intersection)

        assert(
            HitRecord(
                worldPoint = Point(0f, 0f, 0f),
                normal = Normal(0f, 0f, 1f),
                surfacePoint = Vec2d(0f, 0f),
                t = 1f,
                ray = ray1,
                shape = plane1
            ).isClose(intersection)
        )

        val ray2 = Ray(Point(0f, 0f, 1f), Vector(0f, 0f, 1f))
        val ray3 = Ray(Point(1f, 0f, 1f), Vector(1f, 0f, 0f))

        assertNull(plane1.rayIntersection(ray2))
        assertNull(plane1.rayIntersection(ray3))
    }

    @Test
    fun planeRotation() {
        val plane2 = Plane(Rotation(Vector(0f, 1f, 0f), 90f))
        val ray4 = Ray(Point(1f, 0f, 1f), Vector(-1f, 0f, 0f))
        val intersection2 = plane2.rayIntersection(ray4)
        assertNotNull(intersection2)
        assert(
            HitRecord(
                worldPoint = Point(0f, 0f, 1f),
                normal = Normal(1f, 0f, 0f),
                surfacePoint = Vec2d(0f, 0f),
                t = 1f,
                ray = ray4,
                shape = plane2
            ).isClose(intersection2)
        )

        val ray5 = Ray(Point(1f, 0f, 1f), Vector(0f, 1f, 1f))
        val ray6 = Ray(Point(0f, 1f, 0f), Vector(0f, 1f, 0f))
        assertNull(plane2.rayIntersection(ray5))
        assertNull(plane2.rayIntersection(ray6))
    }

    @Test
    fun planeUV() {
        val plane = Plane()
        val ray = Ray(Point(0f, 0f, 1f), Vector(0f, 0f, -1f))
        val intersection = plane.rayIntersection(ray)
        assertNotNull(intersection)
        assert(Vec2d(0f, 0f).isClose(intersection.surfacePoint))

        val ray2 = Ray(Point(0.25f, 0.75f, 1f), Vector(0f, 0f, -1f))
        val intersection2 = plane.rayIntersection(ray2)
        assertNotNull(intersection2)
        assert(Vec2d(0.25f, 0.75f).isClose(intersection2.surfacePoint))

        val ray3 = Ray(Point(4.25f, 7.75f, 1f), Vector(0f, 0f, -1f))
        val intersection3 = plane.rayIntersection(ray3)
        assertNotNull(intersection3)
        assert(Vec2d(0.25f, 0.75f).isClose(intersection3.surfacePoint))
    }

    @Test
    fun pointInternal() {
        val plane1 = Plane()

        val point1 = Point(4f, 5f, 1e-7f)
        val point2 = Point(8f, 9f, 1e-3f)

        assert(plane1.pointInternal(point1))
        assert(!plane1.pointInternal(point2))

        val plane2 = Plane(Rotation(Vector(0f, 1f, 0f), 90f))

        val point3 = Point(1e-5f, 4.6f, 9f)
        val point4 = Point(1f, 0.1f, 45f)

        assert(plane2.pointInternal(point3))
        assert(!plane2.pointInternal(point4))

    }
}