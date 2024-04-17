class ImageTracer(var image: HdrImage, var camera: Camera) {

    fun fireRay(col: Int, row: Int, uPixel: Float=0.5f, vPixel: Float=0.5f): Ray {
        val u = (col + uPixel) / (this.image.width - 1)
        val v = (row + vPixel) / (this.image.height - 1)
            return this.camera.fireRay(u,v)
    }

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

