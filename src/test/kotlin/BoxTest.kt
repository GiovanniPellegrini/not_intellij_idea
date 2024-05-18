import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class BoxTest {

    @Test
    fun boxListIntersection(){
        val cube=Box(Pmin=Point(-1f,-1f,-1f), Pmax = Point(1f,1f,1f))
        val ray=Ray(Point(2f,0f,0f),Vector(-1f,0f,0f))

        val hits=cube.rayIntersectionList(ray)

        assertNotNull(hits!![0])
        val hit1=HitRecord(
            worldPoint = Point(1f,0f,0f),
            normal = Normal(1f,0f,0f),
            surfacePoint = Vec2d(0f,0f),
            t=1f,
            ray=ray,
            shape = cube
        )
        assert(hits[0].isClose(hit1))

        assertNotNull(hits!![1])
        val hit2=HitRecord(
            worldPoint = Point(-1f,0f,0f),
            normal = Normal(1f,0f,0f),
            surfacePoint = Vec2d(0f,0f),
            t=3f,
            ray=ray,
            shape = cube
        )
    }

}