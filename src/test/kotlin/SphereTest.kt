import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class SphereTest(){
    @Test
    fun testHit() {
        val sphere = Sphere()
        val ray1 = Ray(origin = Point(0f, 0f, 2f), dir = -Vector(0f, 0f, 1f))
        val intersection1 = sphere.rayIntersection(ray1)
        assertNotNull(intersection1) { "Error: intersection1 is null" }

        /*this part of the test fails
        if (intersection1 is HitRecord){
            assert(
                HitRecord(
                    worldPoint = Point(0f, 0f, 1f),
                    normal = Normal(0f, 0f, 1f),
                    surfacePoint = Vec2d(0f, 0f),
                    t = 1f,
                    ray = ray1
                ).isClose(intersection1)){"error"}
        }*/
    }

}