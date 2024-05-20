/*
in triangle mash we have a list of vertices like this:
    1st vertex: (x0,y0,z0)
    2nd vertex: (x1,y1,z1)
    3rd vertex: (x2,y2,z2)
    ...

    and a list of indices like this:
    1st triangle: i = 0,1,2
    2nd triangle: i = 1,4,5
    3rd triangle: i = ...
 */

class TriangleMesh(val vertices: Array<Point>, val indices: Array<Array<Int>>,
                   val transformation: Transformation = Transformation(),
                   override val material: Material = Material()): Shape {

    override fun rayIntersection(ray : Ray): HitRecord? {
        for(i in indices.indices){
            val v0 = vertices[indices[i][0]]
            val v1 = vertices[indices[i][1]]
            val v2 = vertices[indices[i][2]]
            val tri = Triangle(a = v0,b = v1, c = v2, material = material,transformation = transformation)
            val hit = tri.rayIntersection(ray)
            if(hit != null){
                return hit
            }
        }
        return null
    }
}