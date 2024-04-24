import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class HomMatrixTest{
    private val sampleMat = HomMatrix()
    private val oneMat = HomMatrix(1f)

    @Test
    fun isCloseTest(){
        assert(sampleMat.isClose(HomMatrix())){"Error: Matrices are not equal"}
    }

    @Test
    fun productTest(){
        assert((sampleMat*sampleMat).isClose(sampleMat))
    }

    @Test
    fun getSetTests(){
        val setMat = HomMatrix(0f)
        assertEquals(oneMat[1],oneMat[2])
        assertEquals(oneMat[1,2],oneMat[2,3])
        setMat[5] = 2f
        setMat[1,0] = 2.0f
        assertEquals(setMat[5],setMat[1,0])
    }
}