import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MathTest {
    @Test
    fun onbTest() {
        val pcg = PCG()

        for (i in 0 until 10000) {
            val normal = Vector(pcg.randomFloat(), pcg.randomFloat(), pcg.randomFloat())

            normal.normalize()

            val onb = onbFromZ(normal)
            val delta = 1e-4f


            assertEquals(onb.third, normal)
            assert(onb.first.xTimes(onb.second).isClose(onb.third))

            assert(onb.first * onb.second < delta)
            assert(onb.second * onb.third < delta)
            assert(onb.third * onb.first < delta)

            assert(are_close(onb.first.sqNorm(), 1f))
            assert(are_close(onb.second.sqNorm(), 1f))

            assert(are_close(onb.third.sqNorm(), 1f))


            assert(onb.first.xTimes(onb.second).isClose(onb.third))
            assert(onb.second.xTimes(onb.third).isClose(onb.first))
            assert(onb.third.xTimes(onb.first).isClose(onb.second))
        }


    }
}


