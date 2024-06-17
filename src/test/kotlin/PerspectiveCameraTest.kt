import org.junit.jupiter.api.Test
import kotlin.math.PI


class PerspectiveCameraTest {

    @Test
    fun perspectiveCameraTest() {
        val cam = PerspectiveCamera(distance = 1.0f, aspectRatio = 2.0f)
        val ray1 = cam.fireRay(0.0f, 0.0f)
        val ray2 = cam.fireRay(1.0f, 0.0f)
        val ray3 = cam.fireRay(0.0f, 1.0f)
        val ray4 = cam.fireRay(1.0f, 1.0f)

        assert(ray1.origin.isClose(ray2.origin))
        assert(ray1.origin.isClose(ray3.origin))
        assert(ray1.origin.isClose(ray4.origin))

        assert(ray1.at(1.0f).isClose(Point(0.0f, 2.0f, -1.0f)))
        assert(ray2.at(1.0f).isClose(Point(0.0f, -2.0f, -1.0f)))
        assert(ray3.at(1.0f).isClose(Point(0.0f, 2.0f, 1.0f)))
        assert(ray4.at(1.0f).isClose(Point(0.0f, -2.0f, 1.0f)))


    }

    @Test
    fun perspectiveCameraTransformTest() {
        val cam = PerspectiveCamera(
            transformation = Translation(-Vector(0f, 1f, 0f) * 2.0f) * Rotation(
                Vector(0f, 0f, 1f),
                theta = 90f.toFloat()
            )
        )
        val ray = cam.fireRay(0.5f, 0.5f)
        assert(ray.at(1.0f).isClose(Point(0.0f, -2.0f, 0.0f)))
    }

}