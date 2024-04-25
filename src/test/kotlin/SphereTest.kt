import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class SphereTest(){
    @Test
    fun testHit() {
        val sphere = Sphere()

        val ray1 = Ray(origin = Point(0f, 0f, 2f), dir = -Vector(0f, 0f, 1f))
        val intersection1 = sphere.rayIntersection(ray1)
        assertNotNull(intersection1) { "Error: intersection1 is null" }

        if (intersection1 is HitRecord){
            assert(HitRecord(
                    worldPoint = Point(0f, 0f, 1f),
                    normal = Normal(0f, 0f, 1f),
                    surfacePoint = Vec2d(0f, 0f),
                    t = 1f,
                    ray = ray1
            ).isClose(intersection1))
        }

        val ray2 = Ray(origin = Point(3f, 0f, 0f), dir = -Vector(1f, 0f, 0f))
        val intersection2 = sphere.rayIntersection(ray2)
        assertNotNull(intersection1) { "Error: intersection2 is null" }

        if (intersection2 is HitRecord){
            assert(HitRecord(
                worldPoint = Point(1f, 0f, 0f),
                normal = Normal(1f, 0f, 0f),
                surfacePoint = Vec2d(0f, 0.5f),
                t = 2f,
                ray = ray2
            ).isClose(intersection2))
        }

        //verify that doesn't intersect
        assertNull(sphere.rayIntersection(Ray(origin=Point(0f, 10f, 2f), dir=-Vector(0f, 0f, 1f))))
    }

    @Test
    fun testInnerHit(){
        val sphere = Sphere()

        val ray = Ray(origin=Point(0f, 0f, 0f), dir=Vector(1f, 0f, 0f))
        val intersection = sphere.rayIntersection(ray)
        assertNotNull(intersection)
        if(intersection is HitRecord) {
            assert(HitRecord(worldPoint=Point(1.0f, 0.0f, 0.0f),
                normal=Normal(-1.0f, 0.0f, 0.0f),
                surfacePoint=Vec2d(0.0f, 0.5f),
                t=1f,
                ray=ray
            ).isClose(intersection))
        }
    }

    @Test
    fun testTransformation(){
        val sphere = Sphere(transformation=Translation(Vector(10f, 0f, 0f)))

        val ray1 = Ray(origin=Point(10f, 0f, 2f), dir=-Vector(0f, 0f, 1f))
        val intersection1 = sphere.rayIntersection(ray1)
        assertNotNull(intersection1)
        if(intersection1 is HitRecord) {
            assert(
                HitRecord(
                    worldPoint = Point(10.0f, 0.0f, 1.0f),
                    normal = Normal(0.0f, 0.0f, 1.0f),
                    surfacePoint = Vec2d(0.0f, 0.0f),
                    t = 1.0f,
                    ray = ray1,
                ).isClose(intersection1)
            )
        }

        val ray2 = Ray(origin=Point(13f, 0f, 0f), dir=-Vector(1f, 0f, 0f))
        val intersection2 = sphere.rayIntersection(ray2)
        assertNotNull(intersection2)
        if(intersection2 is HitRecord) {
            assert(
                HitRecord(
                    worldPoint = Point(11.0f, 0.0f, 0.0f),
                    normal = Normal(1.0f, 0.0f, 0.0f),
                    surfacePoint = Vec2d(0.0f, 0.5f),
                    t = 2.0f,
                    ray = ray2,
                ).isClose(intersection2)
            )
        }

        //Check if the sphere failed to move by trying to hit the untransformed shape
        assertNull(sphere.rayIntersection(Ray(origin=Point(0f, 0f, 2f), dir=-Vector(0f, 0f, 1f))))

        //Check if the *inverse* transformation was wrongly applied
        assertNull(sphere.rayIntersection(Ray(origin=Point(-10f, 0f, 0f), dir=-Vector(0f, 0f, 1f))))

    }

    @Test
    fun testNormal(){
        val sphere = Sphere(transformation=scalingTransformation(Vector(2.0f, 1.0f, 1.0f)))
        val ray = Ray(origin=Point(1.0f, 1.0f, 0.0f), dir=Vector(-1.0f, -1.0f,0f))
        val intersection = sphere.rayIntersection(ray)
        if(intersection is HitRecord){
            intersection.normal.normalize()
            val normal = Normal(1.0f, 4.0f, 0.0f).normalize()
            assert(intersection.normal.isClose(normal))
        }
    }

    @Test
    fun testNormalDirection() {
        //scaling with negative numbers reverse its reference system
        val sphere = Sphere(transformation=scalingTransformation(Vector(-1.0f, -1.0f, -1.0f)))
        val ray = Ray(origin=Point(0.0f, 2.0f, 0.0f), dir=-Vector(0.0f, -1.0f,0f))
        val intersection = sphere.rayIntersection(ray)
        if(intersection is HitRecord){
            assert(intersection.normal.normalize().isClose(Normal(0.0f, 1.0f, 0.0f).normalize()))
        }
    }

}