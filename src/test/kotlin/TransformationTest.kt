import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TransformationTest{

    private val m=HomMatrix(floatArrayOf(
        1.0f, 2.0f, 3.0f, 4.0f,
        5.0f, 6.0f, 7.0f, 8.0f,
        9.0f, 9.0f, 8.0f, 7.0f,
        6.0f, 5.0f, 4.0f, 1.0f
        )
    )
    private val invm = HomMatrix(
        floatArrayOf(
            -3.75f, 2.75f, -1.0f, 0.0f,
            4.375f, -3.875f, 2.0f, -0.5f,
            0.5f, 0.5f, -1.0f, 1.0f,
            -1.375f, 0.875f, 0.0f, -0.5f
        )
    )
    private val transf1 = Transformation(m.copy(), invm.copy())
    private val transf2 = Transformation(invm.copy(), m.copy())

    private val transf3 = Transformation(HomMatrix(floatArrayOf(
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
    fun matrixTimes(){
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

        val excpetedN2=Normal(-8.75f, 7.75f,-3f)
        assert(excpetedN2.isClose(transf4*Normal(3f,2f,4f)))

    }

    @Test
    fun translationTest(){

        val tr1 = Translation(Vector(1.0f, 2.0f, 3.0f))
        assert(tr1.isConsistent())

        val tr2 = Translation(Vector(4.0f, 6.0f, 8.0f))
        assert(tr2.isConsistent())

        val prod = tr1 * tr2
        assert(prod.isConsistent())

        val expected = Translation(Vector(5.0f, 8.0f, 11.0f))
        assert(prod.isClose(expected))

    }

    @Test
    fun rotationTest() {
        val rotx = Rotation(Vector(1f, 0f, 0f), 180f)
        val roty = Rotation(Vector(0f, 1f, 0f), 180f)
        val rotz = Rotation(Vector(0f, 0f, 1f), 180f)

        val rotIDz = Rotation(Vector(0f, 0f, 1f), theta = 2f * 180f)

        //rotation x PI * null vector
        assertTrue((rotx * Vector()).isClose(Vector()))

        //rotation x PI * (4,9,11) == (4-9,-11)
        assertTrue((rotx * Vector(4f, 9f, 11f)).isClose(Vector(4f, -9f, -11f)))
        println("rotx(PI)*${Vector(4f, 9f, 11f)}")
        println(rotx * Vector(4f, 9f, 11f))

        //rotation y PI * null vector
        assertTrue((roty * Vector()).isClose(Vector()))

        assertTrue((roty * Vector(7f, 21f, 7f)).isClose(Vector(-7f, 21f, -7f)))


        assertTrue((rotz * Vector()).isClose(Vector()))

        assertTrue((rotz * Vector(3f, 7f, 4f)).isClose(Vector(-3f, -7f, 4f)))

        //rotation 2pi
        assertTrue((rotIDz * Vector(6f, 1f, 9f)).isClose(Vector(6f, 1f, 9f)))
    }

    @Test
    fun scalingTransformationTest() {
        val sc:Transformation = scalingTransformation(Vector(2f,3f,4f))
        assertTrue((sc*Vector(5f, 7f, 11f)).isClose(Vector(10f, 21f, 44f)))
    }
    

}