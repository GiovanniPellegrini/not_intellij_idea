import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class HdrImageTest {

    var SampleImage = HdrImage(10,10)

    @Test
    fun getPixel(){
        assert(2 in (0 until 10)) { "Error: x value not valid" }
        assert(3 in (0 until 10)) { "Error: y value not valid" }
    }
    @Test
    fun setPixel(){
        assert(5 in (0 until 10)) { "Error: x value not valid" }
        assert(4 in (0 until 10)) { "Error: y value not valid" }
    }

}
