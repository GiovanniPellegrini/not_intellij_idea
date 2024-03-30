data class Point (var x:Float, var y:Float, var z:Float) {
    /**
     * Null constructor
     */
    constructor() : this(0.0F,0.0F,0.0F)
    /**
     * Converting the object to String
     */
    override fun toString():String{
        return "<x:$x, y=$y, z=$z"
    }


}


