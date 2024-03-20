import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.abs

fun are_close(x: Float, y: Float, epsilon: Float = 1.0E-5F): Boolean {
    return (abs(x-y) <epsilon)
}
class ColorTest{

    //qua ci vorrebbero test di sum e product

    @Test
    fun luminosity(){
        val col1 = Color(1.0F, 2.0F, 3.0F)
        val col2 = Color(9.0F, 5.0F, 7.0F)

        assert(are_close(2.0F,col1.luminosity())){"Error: real and evaluated luminosity  of color 1 are different"}
        assert(are_close(7.0F,col2.luminosity())){"Error: real and evaluated luminosity  of color 2 are different"}

    }




}