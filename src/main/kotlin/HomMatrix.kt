/**
 * homogeneous 4x4 matrix
 */
data class HomMatrix(var elements: FloatArray) {
    init {
        require(elements.size == 16) { "A homogeneous matrix must be 4×4" }
    }

    /**
     * constructor of the identity matrix
     */
    constructor() : this(
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )

    /**
     * constructor of an array with all elements equal to the argument
     */
    constructor(a:Float): this(){
        for(i in elements.indices){
            elements[i]=a
        }
    }

    /**
     * verify if two HomMatrix are equals
     */
    fun isClose(other: HomMatrix,epsilon: Float = 1.0E-5F): Boolean {
        for(i in 0 until 16){
            if(!are_close(this[i], other[i],epsilon)) {
                return false
            }
        }
        return true
    }

    /**
     * get operator overloading with one index
     */
    operator fun get(i: Int): Float{
        return elements[i]
    }

    /**
     * get operator overloading with two indexes
     */
    operator fun get(x: Int, y: Int): Float {
        return elements[x* 4 + y]
    }

    /**
     * set operator overloading with one index
     */
    operator fun set(i: Int, b: Float){
        elements[i] = b
    }

    /**
     * set operator overloading with two indexes
     */
    operator fun set(x: Int,y: Int, b: Float){
        elements[x * 4 + y] = b
    }
    /**
     * row by column product between two HomMatrices
     */
    operator fun times(other: HomMatrix): HomMatrix{
        val result = HomMatrix(0f)
        for(i in 0 until 4) {
            for (j in 0 until 4) {
                for (k in 0 until 4) {
                    result[i,j] += this[i,k] * other[k,j]
                }
            }
        }
        return result
    }

}