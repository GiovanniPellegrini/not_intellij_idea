class PCG(private var initState: ULong = 42u, private var initSeq: ULong = 54u){
    var state: ULong = 0u
    var inc: ULong = 0u
    init{
        this.inc = (initSeq shl 1) or 1u
        this.random()
        this.state += initState
        this.random()
    }

    fun random(): Unit{
        println("hello world!")
    }
}