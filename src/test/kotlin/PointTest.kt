import kotlin.test.assertNotSame

class PointTest {
    fun testPoint(){
        val a = Point(1.0f, 2.0f, 3.0f)
        val b = Point(4.0f, 6.0f, 8.0f)
        assert(a == a)
    }
}