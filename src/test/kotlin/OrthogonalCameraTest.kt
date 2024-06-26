import org.junit.jupiter.api.Test
import kotlin.math.PI

class OrthogonalCameraTest {

    private val cam = OrthogonalCamera(aspectRatio = 2f)
    private val ray1 = cam.fireRay(0f, 0f)
    private val ray2 = cam.fireRay(0f, 1f)
    private val ray3 = cam.fireRay(1f, 0f)
    private val ray4 = cam.fireRay(1f, 1f)

    @Test
    fun parallelRays() {
        assert(areClose(0f, (ray1.dir.xTimes(ray2.dir)).sqNorm()))
        assert(areClose(0f, (ray1.dir.xTimes(ray3.dir)).sqNorm()))
        assert(areClose(0f, (ray1.dir.xTimes(ray4.dir)).sqNorm()))
    }

    @Test
    fun hitPoints() {
        val point1 = Point(0f, 2f, -1f)
        val point2 = Point(0f, 2f, 1f)
        val point3 = Point(0f, -2f, -1f)
        val point4 = Point(0f, -2f, 1f)

        assert(ray1.at(1f).isClose(point1))
        assert(ray2.at(1f).isClose(point2))
        assert(ray3.at(1f).isClose(point3))
        assert(ray4.at(1f).isClose(point4))
    }

    @Test
    fun orthogonalCameraTransformTest() {
        val cam = OrthogonalCamera(
            transformation = Translation(-Vector(0f, 1f, 0f) * 2.0f) * Rotation(
                Vector(0f, 0f, 1f),
                theta = 90f
            )
        )
        val ray = cam.fireRay(0.5f, 0.5f)
        assert(ray.at(1.0f).isClose(Point(0.0f, -2.0f, 0.0f)))
    }
}
