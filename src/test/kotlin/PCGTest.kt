import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PCGTest{
    @Test
    fun test(){
        val pcg = PCG()
        println(pcg.state)
        println(pcg.inc)
    }
}