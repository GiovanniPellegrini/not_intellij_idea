/**
 * Image tracer class. This class connects Camera objects with HdrImage objects
 */

class ImageTracer(private var image: HdrImage, private var camera: Camera) {

    /**
     * returns a Ray for a given position in the image
     */
    fun fireRay(col: Int, row: Int, uPixel: Float=0.5f, vPixel: Float=0.5f): Ray {
        val u = (col + uPixel) / (this.image.width )
        val v = 1- (row + vPixel) / (this.image.height)
            return this.camera.fireRay(u,v)
    }

    /**
     * set all the Pixel of an image with function fireRay
     */
    fun fireAllRays(func: (input: Ray) -> Color) {
        for(row in 0 until (image.height)) {
            for(col in 0 until image.width) {
                val ray = this.fireRay(col, row)
                val color = func(ray)
                this.image.setPixel(col, row, color)
            }
        }
    }
}

