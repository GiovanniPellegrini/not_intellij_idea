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

    @Test
    fun testRayIntersection(){
        val verticesList = mutableListOf(Point(1f,0f,0f), Point(0f,1f,0f), Point(-1f,1f,0f), Point(0f,0f,1f))
        val indicesList = mutableListOf(mutableListOf(0,1,3), mutableListOf(1,2,3), mutableListOf(0,3,2), mutableListOf(0,1,2))
        val mesh = TriangleMesh(vertices = verticesList, indices = indicesList)

        //check bounding box
        assert(Point(1f,1f,1f) == mesh.boundingBox.Pmax)
        assert(Point(-1f,0f,0f) == mesh.boundingBox.Pmin)

        //check ray intersection
        val ray = Ray(Point(0f,0f,0f), Vector(1f,0f,0f))
        val hitRecord = mesh.rayIntersection(ray)
        assert(hitRecord != null)
        if (hitRecord != null) {
            assert(hitRecord.worldPoint == Point(1f,0f,0f))
            assert(hitRecord.t == 1f)
        }
    }

    @Test
    fun testTranslation(){
        val verticesList = mutableListOf(Point(1f,0f,0f), Point(0f,1f,0f), Point(-1f,1f,0f), Point(0f,0f,1f))
        val indicesList = mutableListOf(mutableListOf(0,1,3), mutableListOf(1,2,3), mutableListOf(0,3,2), mutableListOf(0,1,2))
        val mesh = TriangleMesh(vertices = verticesList, indices = indicesList, transformation = Translation(Vector(1f,1f,1f)))

        //check bounding box
        assert(Point(1f,1f,1f) == mesh.boundingBox.Pmax)
        assert(Point(-1f,0f,0f) == mesh.boundingBox.Pmin)

        //check ray intersection
        val ray = Ray(Point(0f,0f,0f), Vector(1f,1f,1f))
        val hitRecord = mesh.rayIntersection(ray)
        assert(hitRecord != null)
        if (hitRecord != null) {
            assert(hitRecord.worldPoint == Point(1.3333334f, 1.3333334f, 1.3333334f))
            assert(hitRecord.t == 1.3333334f)
        }

    }

    /*
    for a given diamond shape we rotate it 90 degrees around the x-axis to evaluate
    if the ray now miss the intersection
     */
    @Test
    fun testRotation(){
        val verticesList = mutableListOf(Point(0f,0f,3f), Point(0f,0.2f,0f),
                                         Point(0.2f,1f,0f), Point(0f,-0.2f,1f),
                                         Point(-0.2f,0f,0f), Point(0f,0f,-3f))

        val indicesList = mutableListOf(mutableListOf(0,1,2), mutableListOf(0,2,3), mutableListOf(0,3,4),
                                        mutableListOf(0,4,1), mutableListOf(5,1,2), mutableListOf(5,2,3),
                                        mutableListOf(5,3,4), mutableListOf(5,4,1))

        val ray = Ray(Point(0f,0f,2f), Vector(1f,0f,0f))

        val mesh = TriangleMesh(vertices = verticesList, indices = indicesList)
        val meshRotated = TriangleMesh(vertices = verticesList, indices = indicesList,
                                transformation = Rotation(Vector(1f,0f,0f), 90f))

        assertNotNull(mesh.rayIntersection(ray))
        assertNull(meshRotated.rayIntersection(ray))
    }
}