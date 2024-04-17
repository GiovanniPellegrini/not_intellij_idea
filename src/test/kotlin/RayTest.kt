import org.junit.jupiter.api.Assertions.*
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
}