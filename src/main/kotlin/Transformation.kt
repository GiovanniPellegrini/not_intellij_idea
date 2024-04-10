open class Transformation(var m: HomMatrix, var invm: HomMatrix) {
    fun isConsistent(): Boolean {
        val prod = m * invm
        return prod.isClose(HomMatrix())
    }

    fun inverse(): Transformation {
        return Transformation(invm, m)
    }

    operator fun times(other: Transformation): Transformation {
        return Transformation(m * other.m, other.invm * invm)
    }

    operator fun times(other: Point): Point {
        val newPoint = Point(
            m[0, 0] * other.x + m[0, 1] * other.y + m[0, 2] * other.z + m[0, 3],
            m[1, 0] * other.x + m[1, 1] * other.y + m[1, 2] * other.z + m[1, 3],
            m[2, 0] * other.x + m[2, 1] * other.y + m[2, 2] * other.z + m[2, 3]
        )

        val a = other.x * m[3, 0] + other.y * m[3, 1] + other.z * m[3, 2] + m[3, 3]
        if (a == 1F) return newPoint
        else return Point(newPoint.x / a, newPoint.y / a, newPoint.z / a)
    }


    operator fun times(other: Vector): Vector {
        val newVector = Vector(
            m[0, 0] * other.x + m[0, 1] * other.y + m[0, 2] * other.z + m[0, 3],
            m[1, 0] * other.x + m[1, 1] * other.y + m[1, 2] * other.z + m[1, 3],
            m[2, 0] * other.x + m[2, 1] * other.y + m[2, 2] * other.z + m[2, 3]
        )

        return newVector
    }
     operator fun times(other:Normal):Normal {
         val newNormal = Normal(
             invm[0, 0] * other.x + invm[0, 1] * other.y + invm[0, 2] * other.z + invm[0, 3],
             invm[1, 0] * other.x + invm[1, 1] * other.y + invm[1, 2] * other.z + invm[1, 3],
             invm[2, 0] * other.x + invm[2, 1] * other.y + invm[2, 2] * other.z + invm[2, 3]
         )
         return newNormal
     }
}



 class Translation(): Transformation(HomMatrix(),HomMatrix()) {
     constructor(vec: Vector):this(){
         m[0,3]=vec.x
         m[1,3]=vec.y
         m[2,3]=vec.z

         invm[0,3]=-vec.x
         invm[1,3]=-vec.y
         invm[2,3]=-vec.z
     }
}
