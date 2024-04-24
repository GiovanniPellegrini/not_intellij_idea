import org.junit.jupiter.api.Test

class ImageTracerTest {

    @Test
    fun testImageTracer() {
        val image = HdrImage(width = 4, height = 2)
        val camera = PerspectiveCamera(aspectRatio = 2f)
        val tracer = ImageTracer(image, camera)

        val ray1 = tracer.fireRay(0, 0, uPixel = 2.5f, vPixel = 1.5f)
        val ray2 = tracer.fireRay(2, 1, uPixel = 0.5f, vPixel = 0.5f)
        assert(ray1.isClose(ray2))

        //define lambda to set constant Color
        val sampleColor = { _: Ray -> Color(1.0f,2.0f,3.0f)}
        //set all pixels with constant Color(1.0f,2.0f,3.0f)
        tracer.fireAllRays(sampleColor)

        for (row in 0 until image.height){
            for(col in 0 until image.width){
                assert(image.getPixel (col, row) == Color(1.0f, 2.0f, 3.0f))
            }
        }
    }
}

