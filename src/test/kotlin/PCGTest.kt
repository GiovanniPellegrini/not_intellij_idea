import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PCGTest {
    @Test
    fun test() {
        val pcg = PCG()
        println(pcg.state)
        println(pcg.inc)
    }

    @Test
    fun testPCG() {
        val pcg = PCG()

        assert(pcg.state == 1753877967969059832u)
        assert(pcg.inc == 109.toULong())

        val expectedValues = listOf<UInt>(
            2707161783u,
            2068313097u,
            3122475824u,
            2211639955u,
            3215226955u,
            3421331566u
        )
        for (expected in expectedValues) {
            assert(expected == pcg.random())
        }
    }
}