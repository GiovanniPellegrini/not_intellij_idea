import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TriangleTest {

    @Test
    fun testTriangleCreation() {
        val triangle = Triangle(a = Point(0f, 0f, 0f), b = Point(1f, 0f, 0f), c = Point(0f, 1f, 0f))
        assertEquals(Point(0f, 0f, 0f), triangle.a)
        assertEquals(Point(1f, 0f, 0f), triangle.b)
        assertEquals(Point(0f, 1f, 0f), triangle.c)
    }

    @Test
    fun testDeterminant3x3() {
        val matrix = arrayOf(
            floatArrayOf(1f, 2f, 3f),
            floatArrayOf(4f, 5f, 6f),
            floatArrayOf(7f, 8f, 9f)
        )
        val det = determinant3x3(matrix)
        assertEquals(0f, det)
    }

    @Test
    fun testRayIntersection() {
        val triangle = Triangle(a = Point(0f, 0f, 0f), b = Point(1f, 0f, 0f), c = Point(0f, 1f, 0f))
        val ray = Ray(origin = Point(0.5f, 0.5f, -1f), dir = Vector(0f, 0f, 1f))
        val hitRecord = triangle.rayIntersection(ray)
        assertNotNull(hitRecord)
        if (hitRecord != null) {
            assertEquals(Point(0.5f, 0.5f, 0f), hitRecord.worldPoint)
        }
    }

    @Test
    fun TestNormal() {
        val triangle = Triangle(
            a = Point(0f, -0.5f, 0f),
            b = Point(0f, 0.5f, 0f),
            c = Point(0f, 0f, 0.5f),
            transformation = Rotation(Vector(0f, 1f, 0f), 90f)
        )
        val ray = Ray(Point(0.25f, 0f, 1f), Vector(0f, 0f, -1f))

        assertNotNull(triangle.rayIntersection(ray))
        assert(Normal(0f, 0f, 1f).isClose(triangle.rayIntersection(ray)!!.normal))
    }


    @Test
    fun kramerRuleTest() {
        var solution = floatArrayOf(0f, 0f, 0f)
        val matrix = arrayOf(
            floatArrayOf(1f, 2f, -1f),
            floatArrayOf(1f, -3f / 2f, 5f / 2f),
            floatArrayOf(1f, -2f, 5f)
        )

        val det = determinant3x3(matrix)
        print("determinant: $det\n")
        assert(!areClose(0f, det)) { "determinant is not close to 0" }


        for (j in 0..2) {
            val newMatrix = matrix.map { it.copyOf() }.toTypedArray()
            for (i in newMatrix.indices) {
                newMatrix[i][j] = floatArrayOf(5f, 3f / 2f, 1f)[i]
            }
            solution[j] = determinant3x3(newMatrix) / det

        }

        for (j in (0..2)) {
            assert(areClose(floatArrayOf(3f, 1f, 0f)[j], solution[j])) { "solution is not close to expected value" }
        }

    }

}
