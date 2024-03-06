/**
 * Class of HDR image
 */
class HdrImage (var ncols: Int,var nrows: Int) {
    /**
     * Initialize image with dimension ncols*nrows
     */
    var pixels = Array(ncols*nrows) {10} //da aggiungere la funzione Color()
    /**
     * Get color of a pixel
     */
    fun getPixel(x: Int, y: Int): Int {
        assert(x in 0 until ncols) { "errore 1" }
        assert(y in 0 until nrows) { "errore 2" }

        return pixels[y * ncols + x]
    }

    /**
     * set Color of a pixel
     */
    fun setPixel(x: Int, y: Int, new_color: Int ) {  //da cambiare argomento di tipo color
        assert(x in 0 until ncols) { "errore 1" }
        assert(y in 0 until nrows) { "errore 2" }

        pixels[y * ncols + x] = new_color
    }
}