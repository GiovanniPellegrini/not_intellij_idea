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
    fun kramerRuleTest() {
        var solution = floatArrayOf(0f, 0f, 0f)
        val matrix = arrayOf(
            floatArrayOf(1f, 2f, -1f),
            floatArrayOf(1f, -3f / 2f, 5f / 2f),
            floatArrayOf(1f, -2f, 5f)
        )

        val det = determinant3x3(matrix)
        print("determinant: $det\n")
        assert(!areClose(0f, det)){"determinant is not close to 0"}


        for (j in 0..2) {
            val newMatrix = matrix.map { it.copyOf() }.toTypedArray()
            for (i in newMatrix.indices) {
                newMatrix[i][j] = floatArrayOf(5f, 3f / 2f, 1f)[i]
            }
            solution[j] = determinant3x3(newMatrix) / det

        }

        for (j in (0..2)) {
            assert(areClose(floatArrayOf(3f, 1f, 0f)[j], solution[j])){"solution is not close to expected value"}
        }

    }

    private fun determinant3x3(matrix: Array<FloatArray>): Float {
        return matrix[0][0]*matrix[1][1]*matrix[2][2]+
                matrix[0][1]*matrix[1][2]*matrix[2][0]+
                matrix[0][2]*matrix[1][0]*matrix[2][1]-
                matrix[0][2]*matrix[1][1]*matrix[2][0]-
                matrix[0][1]*matrix[1][0]*matrix[2][2]-
                matrix[0][0]*matrix[1][2]*matrix[2][1]
    }
}
