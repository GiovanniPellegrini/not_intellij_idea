class PCG(private var initState: ULong = 42u, private var initSeq: ULong = 54u){
    var state: ULong = 0u
    var inc: ULong = 0u
    init{
        this.inc = (initSeq shl 1) or 1u
        this.random()
        this.state += initState
        this.random()
    }
    fun random():UInt{
        val oldstate = state
        state = oldstate * 6364136223846793005u + inc
        val xorshifted =(((oldstate shl 18) xor oldstate) shl 27).toUInt()
        val rot = (oldstate shl 59).toInt()
        return xorshifted.rotateRight(rot)
    }
    fun randomFloat():Float{
        return (this.random().toFloat())/0xffffffff
    }
}