import java.io.BufferedReader
import java.io.FileReader


/**
in TriangleMesh we have a list of vertices (private var vertices) indicating the position of each vertex in the mesh:
1st vertex: (x0,y0,z0)
2nd vertex: (x1,y1,z1)
3rd vertex: (x2,y2,z2)
...

and a list of indices (private var indices) indicating the vertices that form each triangle in the mesh:
1st triangle: i = 0,1,2 (means that the 1st triangle is formed by the vertices 0,1 and 2)
2nd triangle: i = 1,4,5
3rd triangle: i = ...
 **/

/**
 * TriangleMesh class: derived from Shape, it represents a mesh made of triangles
 *
 * @param vertices: list of vertices of the mesh
 * @param indices: list of indices of the vertices that form each triangle
 * @param transformation: transformation applied to the mesh
 * @param material: material of the mesh
 * @param boundingBox: bounding box of the mesh
 */
class TriangleMesh(
    var vertices: MutableList<Point>, var indices: MutableList<MutableList<Int>>,
    override val transformation: Transformation = Transformation(),
    override val material: Material = Material(),
    var boundingBox: Box = Box()
) : Shape {

    init {
        boundingBox = Box(
            Pmin = if (vertices.isNotEmpty()) Point(
                vertices.minOf { it.x },
                vertices.minOf { it.y },
                vertices.minOf { it.z }) else Point(),
            Pmax = if (vertices.isNotEmpty()) Point(
                vertices.maxOf { it.x },
                vertices.maxOf { it.y },
                vertices.maxOf { it.z }) else Point(),
            transformation = transformation,
            //this line is not necessary, but it is useful to visualize the bounding Box of the mesh in the image
            material = Material(emittedRad = UniformPigment(Color(200f, 200f, 200f)))
        )
    }

    /**
     * Secondary constructor: Reads the vertices and indices from a wavefront .obj file
     */
    constructor(
        filename: String, transformation: Transformation = Transformation(),
        material: Material = Material()
    ) : this(
        vertices = mutableListOf(),
        indices = mutableListOf(),
        transformation = transformation,
        material = material,
        boundingBox = Box()
    ) {

        val reader: BufferedReader?
        try {
            //read a line of the file
            reader = BufferedReader(FileReader(filename))
            var line = reader.readLine()
            do {
                //if the line starts with "v " it means that the line contains the coordinates of a vertex
                if (line.startsWith("v ")) {
                    // split the line in words and take the 2nd, 3rd and 4th word as the coordinates of the vertex
                    val vertex = line.split("\\s+".toRegex())
                    val x = vertex[1].toFloat()
                    val y = vertex[2].toFloat()
                    val z = vertex[3].toFloat()
                    vertices += Point(x, y, z)
                    //if the line starts with "f " it means that the line contains the indices of the vertices of a triangle
                } else if (line.startsWith("f ")) {
                    //val modifiedLine = line.substring(6) // start reading from the 6th character
                    val index = line.split("\\s+".toRegex())
                    val i0 = index[1].toInt()
                    val i1 = index[2].toInt()
                    val i2 = index[3].toInt()
                    indices += mutableListOf(i0 - 1, i1 - 1, i2 - 1) // -1 because the indices in the file start from 1
                }
                line = reader.readLine()
            } while (line != null)
        } catch (e: NoSuchFileException) {
            println("Error: file not found")
        }
        boundingBox = Box(
            Pmin = Point(vertices.minOf { it.x }, vertices.minOf { it.y }, vertices.minOf { it.z }),
            Pmax = Point(vertices.maxOf { it.x }, vertices.maxOf { it.y }, vertices.maxOf { it.z }),
            transformation = transformation,
            material = Material(emittedRad = UniformPigment(Color(200f, 200f, 200f)))
        )
    }

    /**
     * Returns the closest intersection of a ray with the mesh
     */
    override fun rayIntersection(ray: Ray): HitRecord? {
        boundingBox.rayIntersection(ray) ?: return null // if the ray does not intersect the bounding box, return null
        for (i in indices.indices) {
            val v0 = vertices[indices[i][0]]
            val v1 = vertices[indices[i][1]]
            val v2 = vertices[indices[i][2]]
            val tri = Triangle(a = v0, b = v1, c = v2, material = material, transformation = transformation)
            val hit = tri.rayIntersection(ray)
            if (hit != null) {
                return hit
            }
        }
        return null
    }

    /**
     * Returns all the intersections of a ray with the mesh
     */
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        val hits = mutableListOf<HitRecord>()
        if (this.rayIntersection(ray) == null) return null
        else {
            hits.add(this.rayIntersection(ray)!!)
            return hits
        }
    }

    /**
     * Returns always false, triangles cannot be used for CSG
     */
    override fun pointInternal(point: Point): Boolean {
        return false
    }
}