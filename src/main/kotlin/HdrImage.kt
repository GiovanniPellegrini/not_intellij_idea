/**
 * Class of HDR image
 */
class HdrImage (var width: Int,var height: Int) {
    /**
     * Initialize image with dimension width*height
     */
    var pixels = Array(width*height) {Color(1.0F,1.0F ,1.0F)}
    /**
     * Get color of a pixel
     */
    fun getPixel(x: Int, y: Int): Color {
        return pixels[y * width + x]
    }

    /**
     * Set Color of a pixel
     */
    fun setPixel(x: Int, y: Int, new_color: Color ) {
        pixels[y * width + x] = new_color
    }

}