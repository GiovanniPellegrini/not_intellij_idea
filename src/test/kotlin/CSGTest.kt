import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CSGUnionTest(){

    @Test
    fun pointInternal(){
        val sphere1=Sphere(Translation(Vector(0f,-0.5f,0f)))
        val sphere2=Sphere(Translation(Vector(0f,0.5f,0f)))
        val union=CSGUnion(sphere1,sphere2)

        val point1=Point()
        val point2=Point(0f,0f,1f)

        assertTrue(union.pointInternal(point1))
        assertFalse(union.pointInternal(point2))
    }

    @Test
    fun rayIntersection(){
        val sphere1=Sphere(Translation(Vector(0f,-0.5f,0f)))
        val sphere2=Sphere(Translation(Vector(0f,0.5f,0f)))
        val union=CSGUnion(sphere1,sphere2)

        val ray=Ray(Point(0f,2f,0f),Vector(0f,-1f,0f))

        val intersection=union.rayIntersection(ray)
        assertNotNull(intersection)
        assert(HitRecord(
            worldPoint = Point(0f,1.5f,0f),
            normal = Normal(0f,1f,0f),
            surfacePoint = Vec2d(0.25f,0.5f),
            t=0.5f,
            ray=ray,
            shape = union
        ).isClose(intersection))
    }

    @Test
    fun transformationTest(){
        val sphere1=Sphere(Translation(Vector(0f,-0.5f,0f)))
        val sphere2=Sphere(Translation(Vector(0f,0.5f,0f)))
        val union=CSGUnion(sphere1,sphere2, transformation = Translation(Vector(1f,0f,1f)))

        val ray=Ray(Point(4f,0f,1f),Vector(-1f,0f,0f))
        val intersection=union.rayIntersection(ray)
        assertNotNull(intersection)

    }

    @Test
    fun quickRayIntersectionTest(){
        val sphere1=Sphere()
        val sphere2=Sphere(Translation(Vector(0f,1.7f,0f)))
        val union=CSGUnion(sphere1,sphere2)

        val ray1=Ray(Point(3f,0f,0f),Vector(-1f,0f,0f))
        assert(union.quickRayIntersection(ray1))

        val ray2 = Ray(Point(6f, 1.7f, 0f), Vector(-1f,0f,0f));
        assert(union.quickRayIntersection(ray2))
        assert(!union.quickRayIntersection(Ray(Point(0f, 10f, 2f), Vector(0f,0f,-1f))))
    }
}

class CSGIntersectionTest(){

    @Test
    fun pointInternal(){
        val sphere1=Sphere(Translation(Vector(0f,-0.5f,0f)))
        val sphere2=Sphere(Translation(Vector(0f,0.5f,0f)))
        val intersection=CSGUnion(sphere1,sphere2)

        val point1=Point()
        val point2=Point(0f,0f,0.95f)

        assertTrue(intersection.pointInternal(point1))
        assertFalse(intersection.pointInternal(point2))
    }
    @Test
    fun rayIntersection() {
        val sphere1 = Sphere(Translation(Vector(0f, 0.5f, 0f)))
        val sphere2 = Sphere(Translation(Vector(0f, -0.5f, 0f)))
        val intersection = CSGIntersection(sphere1, sphere2)

        val ray=Ray(Point(0f,2f,0f),Vector(0f,-1f,0f))
        val intersectionpoint=intersection.rayIntersection(ray)
        assertNotNull(intersectionpoint)

        assertTrue(HitRecord(
            worldPoint = Point(0f,0.5f,0f),
            normal = Normal(0f,1f,0f),
            surfacePoint = Vec2d(0.25f,0.5f),
            t=1.5f,
            ray=ray,
            shape = intersection
        ).isClose(intersectionpoint))
    }

    @Test
    fun transformationTest(){
        val sphere1=Sphere(Translation(Vector(0f,-0.5f,0f)))
        val sphere2=Sphere(Translation(Vector(0f,0.5f,0f)))
        val union=CSGIntersection(sphere1,sphere2, transformation = Translation(Vector(1f,0f,1f)))

        val ray=Ray(Point(4f,0f,1f),Vector(-1f,0f,0f))
        val intersection=union.rayIntersection(ray)
        assertNotNull(intersection)
    }

    @Test
    fun quickRayIntersectionTest(){
        val s1 = Sphere(Translation(Vector(0.5f, 0.0f, 0.0f)))
        val s2 = Sphere(Translation(Vector(-0.5f, 0.0f, 0.0f)))
        val u1 = CSGIntersection(s1, s2)

        val r1 = Ray(origin=Point(1.0f, 0.0f, 0.0f), dir=Vector(-1.0f, 0.0f, 0.0f))
        assert(u1.quickRayIntersection(r1))

        val r2 = Ray(origin=Point(12.0f, 12.0f, 10.0f), dir=Vector(0.0f, 0.0f, 1.0f))
        assert(!u1.quickRayIntersection(r2))

        val r3 = Ray(origin=Point(1.0f, 0.0f, 1.0f), dir=Vector(0.0f, 0.0f, -1.0f));
        assert(!u1.quickRayIntersection(r3))
    }
}

class CSGDifferenceTest(){
    @Test
    fun pointInternal(){
        val sphere1=Sphere(Translation(Vector(0f,-0.5f,0f)))
        val sphere2=Sphere(Translation(Vector(0f,0.5f,0f)))
        val intersection=CSGDifference(sphere1,sphere2)

        val point1=Point()
        val point2=Point(0f,-0.5f,-0.95f)

        assertTrue(intersection.pointInternal(point2))
        assertFalse(intersection.pointInternal(point1))

    }

    @Test
    fun rayIntersection(){
        val sphere1=Sphere(Translation(Vector(0f,-0.5f,0f)))
        val sphere2=Sphere(Translation(Vector(0f,0.5f,0f)))
        val difference=CSGDifference(sphere1,sphere2)

        val ray=Ray(Point(0f,2f,0f),Vector(0f,-1f,0f))

        val intersection=difference.rayIntersection(ray)
        assertNotNull(intersection)
        assertTrue(HitRecord(
            worldPoint = Point(0f,-0.5f,0f),
            normal = Normal(0f,1f,0f),
            surfacePoint = Vec2d(0.75f,0.5f),
            t=2.5f,
            ray=ray,
            shape = difference
        ).isClose(intersection))
    }

    @Test
    fun transformationTest(){
        val sphere1=Sphere(Translation(Vector(0f,-0.5f,0f)))
        val sphere2=Sphere(Translation(Vector(0f,0.5f,0f)))
        val difference=CSGDifference(sphere1,sphere2, Translation(Vector(1f,0f,1f)))

        val ray=Ray(Point(4f,-0.5f,1f),Vector(-1f,0f,0f))

        val intersection=difference.rayIntersection(ray)
        assertNotNull(intersection)
    }

    @Test
    fun quickRayIntersectionTest(){
        val s1 = Sphere(transformation=Translation(Vector(0.5f, 0.0f, 0.0f)))
        val s2 = Sphere()
        val u1 = CSGDifference(s1, s2);

        val r1 = Ray(origin = Point(0.0f, 0.0f, 0.0f), dir = Vector(1.0f, 0.0f, 0.0f))
        assert(u1.quickRayIntersection(r1)){"failed first int"}

        val r2 = Ray(origin = Point(12.0f, 12.0f, 10.0f), dir = Vector(0.0f, 0.0f, 1.0f))
        assert(!u1.quickRayIntersection(r2)){"failed second int"}

        val r3 = Ray(origin = Point(0.0f, 0.0f, 1.0f), dir = Vector(0.0f, 0.0f, -1.0f));
        assert(!u1.quickRayIntersection(r3)){ "failed third int" }
    }
}








































