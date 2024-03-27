import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.pow
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
    fun vectorsOperationsTest(){
        assert((-a).isClose(Vector(-1.0f, -2.0f, -3.0f)))
        assert((a+b).isClose(Vector(5.0f, 8.0f, 11.0f)))
        assert((b-a).isClose(Vector(3.0f, 4.0f, 5.0f)))
        assertEquals(40.0f, a*b)
        assert(a.xTimes(b).isClose(Vector(-2.0f, 4.0f, -2.0f)))
        assert(b.xTimes(a).isClose(Vector(2.0f, -4.0f, 2.0f)))
        assert(are_close(a.sqNorm(),14.0f))
        assert(are_close(a.norm().pow(2),14.0f))
    }

}