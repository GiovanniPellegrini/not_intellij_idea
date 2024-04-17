import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
class OrthogonalCameraTest {

    val cam=OrthogonalCamera(aspectRatio = 2f)
    val ray1=cam.fireRay(0f,0f)
    val ray2=cam.fireRay(0f,1f)
    val ray3=cam.fireRay(1f,0f)
    val ray4=cam.fireRay(1f,1f)
    @Test
    fun parallelRays(){



        assert(are_close(0f, (ray1.dir.xTimes(ray2.dir)).sqNorm()))
        assert(are_close(0f, (ray1.dir.xTimes(ray3.dir)).sqNorm()))
        assert(are_close(0f, (ray1.dir.xTimes(ray4.dir)).sqNorm()))
    }

    @Test
    fun hitPoints(){
        val point1=Point(0f,2f,-1f)
        val point2=Point(0f,2f,1f)
        val point3=Point(0f,-2f,-1f)
        val point4=Point(0f,-2f,1f)

        assert(ray1.at(1f).isClose(point1))
        assert(ray2.at(1f).isClose(point2))
        assert(ray3.at(1f).isClose(point3))
        assert(ray4.at(1f).isClose(point4))
    }
}
