/**
 * homogeneous 4x4 matrix
 */
data class HomMatrix(var elements: FloatArray) {
    init {
        require(elements.size == 16) { "A homogeneous matrix must be 4×4" }
    }

    constructor() : this(
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )
    operator fun get(x: Int, y: Int): Float {
        return elements[y * 4 + x]
    }
}