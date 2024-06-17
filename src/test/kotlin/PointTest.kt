import org.junit.jupiter.api.Test

class PointTest {
    @Test
    fun testPoint() {
        val p1 = Point(1.0f, 2.0f, 3.0f)
        val p2 = Point(4.0f, 6.0f, 8.0f)
        assert(p1 == p1)
        assert(p1 != p2)
    }

    @Test
    fun testPointOperations() {
        val p1 = Point(1.0f, 2.0f, 3.0f)
        val p2 = Point(4.0f, 6.0f, 8.0f)
        val v = Vector(4.0f, 6.0f, 8.0f)

        assert((p1 * 2f).isClose(Point(2.0f, 4.0f, 6.0f)))
        assert((p1 + v).isClose(Point(5.0f, 8.0f, 11.0f)))
        assert((p2 - p1).isClose(Vector(3.0f, 4.0f, 5.0f)))
        assert((p1 - v).isClose(Point(-3.0f, -4.0f, -5.0f)))
        assert((-p1).isClose(Point(-1.0f, -2.0f, -3.0f)))
    }
}