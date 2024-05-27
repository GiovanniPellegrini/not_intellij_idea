import org.junit.jupiter.api.Test

class ColorTest{

    @Test
    fun equal(){
        val color1=Color(1F,2F,3F)
        val color2=Color(1F,2F,3F)
        assert(color1==color2)
    }

    @Test
    fun sum(){
        val color1=Color(1F,2F,3F)
        val color2=Color(3F,4F,5F)
        assert(Color(4.0F,6.0F,8.0F)==color2+color1)
    }

    @Test
    fun areCloseColor(){
        val delta=1e-6F
        val color1=Color(1F,2F,3F)
        val color2=Color(1F+delta,2F+delta,3F+delta)
        assert(color1.areClose(color2))
    }

    @Test
    fun scalarProduct(){
        val color1=Color(1F,2F,6F)
        val scalar=0.5F
        assert(Color(0.5F,1F,3F)==color1.scalarProduct(scalar))
        assert(Color(0.5F,1F,3F)==color1*scalar)
    }

    @Test
    fun colorProduct(){
        val color1=Color(1F,2F,3F)
        val color2=Color(4F,5F,6F)
        assert(Color(4F,10F,18F)==color1.colorProduct(color2))
        assert(Color(4F,10F,18F)==color1*color2)
    }

    @Test
    fun luminosity(){
        val col1 = Color(1.0F, 2.0F, 3.0F)
        val col2 = Color(9.0F, 5.0F, 7.0F)

        assert(areClose(2.0F,col1.luminosity())){"Error: real and evaluated luminosity  of color 1 are different"}
        assert(areClose(7.0F,col2.luminosity())){"Error: real and evaluated luminosity  of color 2 are different"}

    }
}