import java.io.BufferedReader
import java.io.FileReader

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

class TriangleMesh(var vertices: Array<Point>, var indices: Array<Array<Int>>,
                   val transformation: Transformation = Transformation(),
                   override val material: Material = Material())
    : Shape {

    constructor(filename : String, transformation: Transformation = Transformation(),
                material: Material = Material()): this(
                vertices = Array<Point>(0) { Point() }, // Inizializza un array vuoto di punti
                indices = Array<Array<Int>>(0) { arrayOf(0) },
                transformation = transformation,
                material = material) {

        val reader: BufferedReader?
        try{
            reader = BufferedReader(FileReader(filename))
            var line = reader.readLine()
            do{
                if(line.startsWith("v ")){
                    val vertex = line.split(" ")
                    val x = vertex[1].toFloat()
                    val y = vertex[2].toFloat()
                    val z = vertex[3].toFloat()
                    vertices += Point(x,y,z)
                }else if(line.startsWith("f ")){
                    //val modifiedLine = line.substring(6) // start reading from the 6th character
                    val index = line.split(" ")
                    val i0 = index[1].toInt()
                    val i1 = index[2].toInt()
                    val i2 = index[3].toInt()
                    indices += arrayOf(i0-1,i1-1,i2-1)
                }
                line = reader.readLine()
            }while(line != null)
        }catch(e: NoSuchFileException){
            println("Error: file not found")
        }
    }



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