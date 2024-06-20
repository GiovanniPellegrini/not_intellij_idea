import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.pow

class VectorTest {

    private val a = Vector(1.0f, 2.0f, 3.0f)
    private val b = Vector(4.0f, 6.0f, 8.0f)

    @Test
    fun vectorsTest() {
        assert(a.isClose(a))
        assert(!a.isClose(b))
    }

    @Test
    fun vectorsOperationsTest() {
        assert((-a).isClose(Vector(-1.0f, -2.0f, -3.0f)))
        assert((a + b).isClose(Vector(5.0f, 8.0f, 11.0f)))
        assert((b - a).isClose(Vector(3.0f, 4.0f, 5.0f)))
        assertEquals(40.0f, a * b)
        assert(a.xTimes(b).isClose(Vector(-2.0f, 4.0f, -2.0f)))
        assert(b.xTimes(a).isClose(Vector(2.0f, -4.0f, 2.0f)))
        assert(areClose(a.sqNorm(), 14.0f))
        assert(areClose(a.norm().pow(2), 14.0f))
    }

}