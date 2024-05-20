import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class BoxTest {

    @Test
    fun testPointInternal(){
        val box = Box(Pmax = Point(2f,1f,1f), Pmin = Point(0f,0f,0f))
        val point = Point(1f,1f,0.5f)
        assert(box.pointInternal(point))
    }

    @Test
    fun testRayIntersection(){
        /*
        this is the configuration of the ray intersection, where O is the origin of the ray
        and I is the intersection point, y coordinate is fixed to 1

                              ^ z
            O_________________| 2
            |                 |
            |                 |
            |     ------I-----| 1
            |     |    BOX    |
        x<--------------------|
            3     2


         */
        val dir = Vector(-2f,0f,-1f)
        dir.normalize()
        val box = Box(Pmax = Point(2f,1f,1f), Pmin = Point(0f,0f,-1f))
        val ray = Ray(Point(3f,1f,2f), dir = dir)
        val hit = box.rayIntersection(ray)
        assertNotNull(hit)
        val hit1 = HitRecord(
            worldPoint = Point(1.8f,1f,1f),
            normal = Normal(0f,0f,1f),
            surfacePoint = Vec2d(1.8f,1f),
            t=1.341640f,
            ray=ray,
            shape = box
        )
        assert(hit.isClose(hit1))
    }

    @Test
    fun testRayIntersectionTranslated(){
        val dir = Vector(-2f,0f,-1f)
        dir.normalize()
        val box = Box(Pmax = Point(2f,1f,1f), Pmin = Point(0f,0f,-1f), transformation = Translation(Vector(0f,1f,0f)))
        val ray = Ray(Point(3f,2f,2f), dir = dir)
        val hit = box.rayIntersection(ray)
        assertNotNull(hit)
        val hit1 = HitRecord(
            worldPoint = Point(1.8f,2f,1f),
            normal = Normal(0f,0f,1f),
            surfacePoint = Vec2d(1.8f,1f),
            t=1.341640f,
            ray=ray,
            shape = box
        )
        assert(hit.isClose(hit1))
    }

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

        assertNotNull(hits[1])
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