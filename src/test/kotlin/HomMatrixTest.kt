import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class HomMatrixTest{
    val sampleMat = HomMatrix()
    val product = sampleMat*sampleMat
    @Test
    fun productTest(){
        assert((sampleMat*sampleMat).isClose(sampleMat))
    }



}