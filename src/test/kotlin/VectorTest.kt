import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertIsNot

class VectorTest{

    val a = Vector(1.0f, 2.0f, 3.0f)
    val b = Vector(4.0f, 6.0f, 8.0f)
    @Test
    fun VectorsTest() {
        assert(a.isClose(a))
        assert(a.isClose(b).not())
    }

    @Test
    fun VectorsOperationsTest(){
        assert((-a).isClose(Vector(-1.0f, -2.0f, -3.0f)))
        assert((a+b).isClose(Vector(5.0f, 8.0f, 11.0f)))
        assert((b-a).isClose(Vector(3.0f, 4.0f, 5.0f)))
        //Da terminare
    }

}