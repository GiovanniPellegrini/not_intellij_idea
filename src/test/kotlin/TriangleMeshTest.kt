import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TriangleMeshTest{
    @Test
    fun testSecondaryConstructor(){
        val mesh = TriangleMesh("src/test/kotlin/tetrahedron.obj")
        assertEquals(4, mesh.vertices.size)
        assertEquals(4, mesh.indices.size)

        assertEquals(Point(1f, 2f, 1f), mesh.vertices[2])
        assertEquals(mutableListOf(0,3,2), mesh.indices[1])

        assertEquals(Point(2f, 2f, 2f), mesh.boundingBox.Pmax)
        assertEquals(Point(1f, 1f, 1f), mesh.boundingBox.Pmin)
    }
}