import org.junit.jupiter.api.Test


class PerspectiveCameraTest{

    @Test
    fun perspectiveCameraTest(){
        val cam = PerspectiveCamera(distance = 1.0f, aspectRatio = 2.0f)
        val ray1 = cam.fireRay(0.0f, 0.0f)
        val ray2 = cam.fireRay(1.0f, 0.0f)
        val ray3 = cam.fireRay(0.0f, 1.0f)
        val ray4 = cam.fireRay(1.0f, 1.0f)

        //Verify that all the rays depart from the same point
        assert(ray1.origin.isClose(ray2.origin))
        assert(ray1.origin.isClose(ray3.origin))
        assert(ray1.origin.isClose(ray4.origin))

        //Verify that the ray hitting the corners have the right coordinates
        assert(ray1.at(1.0f).isClose(Point(0.0f, 2.0f, -1.0f)))
        assert(ray2.at (1.0f).isClose(Point(0.0f, -2.0f, -1.0f)))
        assert(ray3.at(1.0f).isClose(Point(0.0f, 2.0f, 1.0f)))
        assert(ray4.at(1.0f).isClose(Point(0.0f, -2.0f, 1.0f)))
    }

}