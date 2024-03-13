/**
Class Color
**/

class Color(var r:Float, var g:Float, var b:Float) {
   /**
    Sum of two colors
   **/
   fun sum(other:Color):Color {
        return Color(r + other.r, g + other.g, b + other.b)
   }

    /**
     * Boolean to understand if two colors are close
     */
    fun areCloseColor(other:Color):Boolean{
        return (are_close(r,other.r) &&
                are_close(g,other.g) &&
                are_close(b,other.b))
    }

}
