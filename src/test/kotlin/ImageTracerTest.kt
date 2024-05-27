import org.junit.jupiter.api.Test

class ImageTracerTest {

    private val image = HdrImage(width = 4, height = 2)
    private val camera = PerspectiveCamera(aspectRatio = 2f)
    private val tracer = ImageTracer(image, camera)

    @Test
    fun testOrientation() {
        val topLeftRay = tracer.fireRay(0, 0, uPixel = 0.0f, vPixel = 0.0f)
        assert(Point(0f, 2f, 1f).isClose(topLeftRay.at(1f))){"flipped image bug"}

        val bottomRightRay = tracer.fireRay(3, 1, uPixel = 1.0f, vPixel = 1.0f)
        assert(Point(0f, -2f, -1f).isClose(bottomRightRay.at(1f))){"flipped image bug"}
    }

    @Test
    fun testUvSubMapping() {
        val ray1 = tracer.fireRay(0, 0, uPixel = 2.5f, vPixel = 1.5f)
        val ray2 = tracer.fireRay(2, 1, uPixel = 0.5f, vPixel = 0.5f)
        assert(ray1.isClose(ray2))
    }

    //define lambda to set constant Color
    private val sampleColor = { _: Ray -> Color(1.0f,2.0f,3.0f)}
    //set all pixels with constant Color(1.0f,2.0f,3.0f)

    @Test
    fun testImageCoverage(){
        tracer.fireAllRays(sampleColor)
        for (row in 0 until image.height){
            for(col in 0 until image.width){
                assert(image.getPixel (col, row) == Color(1.0f, 2.0f, 3.0f))
            }
        }
    }

    @Test
    fun stratifiedSampling(){
        var numOfRay=0
        val smallImage=HdrImage(1,1)
        val camera=OrthogonalCamera(1f)
        val tracer=ImageTracer(smallImage,camera)

        val nullTraceRay:(Ray)->Color={ray->

            val point=ray.at(1f)
            assert(point.x==0f)
            assert(point.y>-1f && point.y<1f)
            assert(point.z>-1f && point.z<1f)
            numOfRay+=1
            Color()
        }
        tracer.fireAllRays(nullTraceRay, raysForSide = 10)
        assert(numOfRay==100)


    }
}

