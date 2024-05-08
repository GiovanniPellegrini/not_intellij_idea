import org.junit.jupiter.api.Test

class RayTest{

    @Test
    fun isCloseTest(){
        val ray1 = Ray(Point(1.0f, 2.0f, 3.0f), Vector(5.0f, 4.0f, -1.0f))
        val ray2 = Ray(Point(1.0f, 2.0f, 3.0f), Vector(5.0f, 4.0f, -1.0f))
        val ray3 = Ray(Point(5.0f, 1.0f, 4.0f),Vector(3.0f, 9.0f, 4.0f))

        assert(ray1.isClose(ray2))
        assert(!ray1.isClose(ray3))

    }

    @Test
    fun atTest(){
        val ray = Ray(Point(1.0f, 2.0f, 4.0f),Vector(4.0f, 2.0f, 1.0f))
        assert(ray.at(0.0f).isClose(ray.origin))
        assert(ray.at(1.0f).isClose(Point(5.0f, 4.0f, 5.0f)))
        assert(ray.at(2.0f).isClose(Point(9.0f, 6.0f, 6.0f)))
    }

    @Test
    fun transformTest(){
        val ray = Ray(Point(1.0f, 2.0f, 3.0f), Vector(6.0f, 5.0f, 4.0f))
        val transform = Translation(Vector(10.0f, 11.0f, 12.0f)) * Rotation(Vector(1f,0f,0f),90f)
        val transformed = ray.transformation(transform)

        assert(transformed.origin.isClose(Point(11.0f, 8.0f, 14.0f))){"Error1"}
        assert(transformed.dir.isClose(Vector(6.0f, -4.0f, 5.0f))){"Error2"}
    }
}