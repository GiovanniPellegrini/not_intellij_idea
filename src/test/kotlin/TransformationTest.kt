import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.math.exp

class TransformationTest{
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
    val transf1=Transformation(m,invm)
    val transf2=Transformation(invm,m)
    val transf3=Transformation(HomMatrix(floatArrayOf(
        3.0f, 5.0f, 2.0f, 4.0f,
        4.0f, 1.0f, 0.0f, 5.0f,
        6.0f, 3.0f, 2.0f, 0.0f,
        1.0f, 4.0f, 2.0f, 1.0f
    )),HomMatrix(floatArrayOf(
        0.4f, -0.2f, 0.2f, -0.6f,
        2.9f, -1.7f, 0.2f, -3.1f,
        -5.55f, 3.15f, -0.4f, 6.45f,
        -0.9f, 0.7f, -0.2f, 1.1f
    )))
    @Test
    fun isConsistent(){
        assert(transf1.isConsistent())
    }
    @Test
    fun inverse(){
        assert(transf1.inverse()==transf2)
    }

    @Test
    fun isClose(){
        assertFalse(transf1.isClose(transf2))
        assert(transf1.isClose(Transformation(m,invm)))
        assert(transf1==Transformation(m,invm))
    }

    @Test
    fun matrixtimes(){
        val mArray = HomMatrix(
            floatArrayOf(
                33.0f, 32.0f, 16.0f, 18.0f,
                89.0f, 84.0f, 40.0f, 58.0f,
                118.0f, 106.0f, 48.0f, 88.0f,
                63.0f, 51.0f, 22.0f, 50.0f
            )
        )
        val invmArray = HomMatrix(
            floatArrayOf(
                -1.45f, 1.45f, -1.0f, 0.6f,
                -13.95f, 11.95f, -6.5f, 2.6f,
                25.525f, -22.025f, 12.25f, -5.2f,
                4.825f, -4.325f, 2.5f, -1.1f
            )
        )
        val expected=Transformation(mArray,invmArray)
        assert(transf3.isConsistent())
        assert(expected.isConsistent(10e-4f))
        assert(expected==transf1*transf3)
    }

    @Test
    fun times(){
        val mArray= HomMatrix(
            floatArrayOf(
                1.0f, 2.0f, 3.0f, 4.0f,
                5.0f, 6.0f, 7.0f, 8.0f,
                9.0f, 9.0f, 8.0f, 7.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        )
        val invmArray=HomMatrix(
            floatArrayOf(
                -3.75f, 2.75f, -1.0f, 0.0f,
                5.75f, -4.75f, 2.0f, 1.0f,
                -2.25f, 2.25f, -1.0f, -2.0f,
                0.0f, 0.0f, 0.0f, 1.0f
            )
        )
        val transf4=Transformation(mArray,invmArray)
        assert(transf4.isConsistent())

        val expectedV=Vector(14f,38f,51f)
        assert(expectedV.isClose(transf4*Vector(1f,2f,3f)))

        val expectedP=Point(18f,46f,58f)
        assert(expectedP.isClose(transf4*Point(1f,2f,3f)))

        val expectedN=Normal(-1.25f, 2.25f, -0.75f)
        assert(expectedN.isClose(transf4*Normal(1f,2f,3f)))

    }













}