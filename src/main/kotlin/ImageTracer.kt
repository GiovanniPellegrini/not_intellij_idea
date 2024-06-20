import kotlin.math.round
/**
 * Image tracer class. This class connects Camera objects with HdrImage objects
 */

class ImageTracer(private val image: HdrImage, private val camera: Camera, private val pcg: PCG=PCG(), val raysForSide:Int=0) {

    /**
     * returns a Ray for a given position in the image
     */
    fun fireRay(col: Int, row: Int, uPixel: Float=0.5f, vPixel: Float=0.5f): Ray {
        val u = (col + uPixel) / (this.image.width )
        val v = 1f - (row + vPixel) / (this.image.height)
        return this.camera.fireRay(u,v)
    }

    /**
     * set all the Pixel of an image with function fireRay
     */
    fun fireAllRays(func: (Ray) -> Color) {
        for(row in 0 until image.height) {
            for(col in 0 until image.width) {
                val ray = fireRay(col, row)
                val color = func(ray)
                image.setPixel(col, row, color)
            }
            val progress = (row.toFloat() / image.height.toFloat()) * 100
            val status = "#".repeat(progress.toInt())
            val remaining = " ".repeat(99 - progress.toInt())
            print("Ray tracing progress: [${status}${remaining}] ${round(progress)}% \r")
        }
        println("Ray tracing progress completed!")
    }

    /**
     *fire rays using Stratified Sampling. raysforSide indicates how many rays are fired for each side of the pixel
     * In total will be fired raysforSideXraysforSide rays for each pixel
     */
    fun fireAllRays(func: (Ray) -> Color,pcg:PCG=PCG(),raysForSide: Int){
        for(row in 0 until image.height) {
            for(col in 0 until image.width) {
                if(raysForSide==1) throw Error("number of ray must be greater than 0")

                var cum=Color()
                for(i in 0 until raysForSide){
                    for(j in 0 until raysForSide){
                        val uPixel= (i+pcg.randomFloat())/raysForSide
                        val vPixel= (j+pcg.randomFloat())/raysForSide
                        val ray=fireRay(col, row, uPixel, vPixel)
                        cum+=func(ray)
                    }
                }
                image.setPixel(col, row, cum*(1f/(raysForSide*raysForSide)))
            }
            val progress = (row.toFloat() / image.height.toFloat()) * 100
            val status = "#".repeat(progress.toInt())
            val remaining = " ".repeat(99 - progress.toInt())
            print("Ray tracing progress: [${status}${remaining}] ${round(progress)}% \r")
        }
        println("Ray tracing progress completed!")
    }

}

