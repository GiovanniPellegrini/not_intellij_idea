import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ImageTracerTest(){

    @Test
    fun testImageTracer() {
        val image = HdrImage(width = 4, height = 2)
        val camera = PerspectiveCamera(aspectRatio = 2f)
        val tracer = ImageTracer(image = image, camera = camera)

        val ray1 = tracer.fireRay(0, 0, uPixel = 2.5f, vPixel = 1.5f)
        val ray2 = tracer.fireRay(2, 1, uPixel = 0.5f, vPixel = 0.5f)
        assert(ray1.isClose(ray2))

        /*
        tracer.fireAllRays( ray=  Color(1.0f, 2.0f, 3.0f))
        for row in range(image.height):
        for col in range(image.width):
        assert image.get_pixel(col, row) == Color(1.0, 2.0, 3.0)*/
    }
}