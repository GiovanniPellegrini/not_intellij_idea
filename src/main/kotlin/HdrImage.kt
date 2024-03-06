/**
 * Class of HDR image
 */
class HdrImage (var ncols: Int,var nrows: Int) {
    var pixels = Array(ncols*nrows) {10}
    /**
     * Get coordinates of pixel
     */
    fun getPixel(x: Int, y: Int): Int {
        assert(x in 0 until ncols) { "errore 1" }
        assert(y in 0 until nrows) { "errore 2" }

        for (i in 0 until ncols*nrows) println(pixels[i])

        return 0
    }
}