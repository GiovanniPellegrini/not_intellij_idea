/**
 * PCG Random Number Generator
 *
 * @param initState: Initial seed of the generator
 * @param initSeq: Initial sequence of the generator
 */

class PCG(private var initState: ULong = 42u, private var initSeq: ULong = 54u) {
    var state: ULong = 0u
    var inc: ULong = 0u

    init {
        this.inc = (initSeq shl 1) or 1u
        this.random()
        this.state += initState
        this.random()
    }

    /**
     * Returns a random number
     */
    fun random(): UInt {
        val oldstate = state
        state = oldstate * 6364136223846793005u + inc
        val xorshifted = (((oldstate shr 18) xor oldstate) shr 27).toUInt()
        val rot = (oldstate shr 59).toInt()
        return xorshifted.rotateRight(rot)
    }

    /**
     * Returns a random float number between 0 and 1
     */
    fun randomFloat(): Float {
        return (this.random().toFloat()) / 0xffffffff
    }
}