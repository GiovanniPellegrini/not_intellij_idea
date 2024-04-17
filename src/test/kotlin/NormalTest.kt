import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.pow

class NormalTest{

    val a = Normal(1.0f, 2.0f, 3.0f)
    val b = Normal(4.0f, 6.0f, 8.0f)
    val c = Vector(4.0f, 6.0f, 8.0f)

    @Test
    fun normalsTest() {
        assert(a.isClose(a))
        assert(a.isClose(b).not())
    }

    @Test
    fun normalsOperationsTest(){
        assert((-a).isClose(Normal(-1.0f, -2.0f, -3.0f)))
        assertEquals(40.0f, a*b)
        assertEquals(a*2f, Normal(2f,4f,6f))
        assert(a.xTimes(b).isClose(Normal(-2.0f, 4.0f, -2.0f)))
        assert(b.xTimes(a).isClose(Normal(2.0f, -4.0f, 2.0f)))
        assert(are_close(a.sqNorm(),14.0f))
        assert(are_close(a.norm().pow(2),14.0f))
    }

}