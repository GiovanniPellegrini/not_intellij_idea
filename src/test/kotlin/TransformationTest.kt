import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TransformationTest{
    @Test
    fun isConsistent(){
        val m=HomMatrix(floatArrayOf(
            1.0f, 2.0f, 3.0f, 4.0f,
            5.0f, 6.0f, 7.0f, 8.0f,
            9.0f, 9.0f, 8.0f, 7.0f,
            6.0f, 5.0f, 4.0f, 1.0f
        ))
        val invm=HomMatrix(
            floatArrayOf(
                -3.75f, 2.75f, -1.0f, 0.0f,
                4.375f, -3.875f, 2.0f, -0.5f,
                0.5f, 0.5f, -1.0f, 1.0f,
                -1.375f, 0.875f, 0.0f, -0.5f
            )
        )
        val tras=Transformation(m,invm)
        assert(tras.isConsistent())
    }
    














}