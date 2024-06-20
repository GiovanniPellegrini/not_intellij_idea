import org.junit.jupiter.api.Test
import kotlin.math.sqrt
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue


class ConeTest {
    @Test
    fun pointInternal() {
        val cone = Cone()
        assertTrue(cone.pointInternal(Point(0f, 0f, 1f)))
        assertTrue(cone.pointInternal(Point(0.25f, 0.25f, 0f)))
        assertFalse(cone.pointInternal(Point(0.5f, 0.5f, 1f)))
    }

    @Test
    fun rayIntersection() {
        val cone = Cone()

        val ray1 = Ray(Point(1.5f, 0f, 0.5f), Vector(-1f, 0f, 0f));
        val hit1 = cone.rayIntersection(ray1);

        assertNotNull(hit1)

        assert(
            hit1.isClose(
                HitRecord(
                    Point(0.5f, 0f, 0.5f),
                    Normal(0.5f, 0f, 0.5f),
                    Vec2d(0f, 0.5f),
                    1f,
                    ray1,
                    cone
                )
            )
        )

        val ray2 = Ray(Point(5f, 0f, 0f), Vector(-1f, 0f, 0f))
        val hit2 = cone.rayIntersection(ray2)

        assertNotNull(hit2)
        assert(
            hit2.isClose(
                HitRecord(
                    worldPoint = Point(1f, 0f, 0f),
                    normal = Normal(1f, 0f, 0f),
                    surfacePoint = Vec2d(0f, 1f),
                    t = 4f,
                    ray = ray2,
                    shape = cone
                )
            )
        )
        val cone2 = Cone(Translation(Vector(2f, 0f, 0f)))
        assertNull(cone2.rayIntersection(ray1))
    }

    @Test
    fun rayIntersectionList() {
        val cone = Cone()
        val ray1 = Ray(Point(1.5f, 0f, 0.5f), Vector(-1f, 0f, 0f))
        val hits = cone.rayIntersectionList(ray1)
        assertNotNull(hits)
        assert(hits.size == 2)
        assert(
            hits[0].isClose(
                HitRecord(
                    Point(0.5f, 0f, 0.5f),
                    Normal(0.5f, 0f, 0.5f),
                    Vec2d(0f, 0.5f),
                    1f,
                    ray1,
                    cone
                )
            )
        )
        println(hits[1].worldPoint)
        println(hits[1].normal)
        println(hits[1].surfacePoint.u)
        println(hits[1].surfacePoint.v)
        println(hits[1].t)


        assert(
            hits[1].isClose(
                HitRecord(
                    Point(-0.5f, 0f, 0.5f),
                    Normal(0.5f, 0f, -0.5f),
                    Vec2d(0.5f, 0.5f),
                    2f,
                    ray1,
                    cone
                )
            )
        )
    }

    @Test
    fun transformation() {
        val cone = Cone(Translation(Vector(1f, 0f, 1f)))
        val ray = Ray(Point(1f, 1f, 1.5f), Vector(0f, -1f, 0f))
        val hit = cone.rayIntersection(ray)

        assertNotNull(hit)
        assert(
            hit.isClose(
                HitRecord(
                    Point(1f, 0.5f, 1.5f),
                    Normal(0f, 0.5f, 0.5f),
                    Vec2d(0.25f, 0.5f),
                    0.5f,
                    ray,
                    cone
                )
            )
        )
    }
}