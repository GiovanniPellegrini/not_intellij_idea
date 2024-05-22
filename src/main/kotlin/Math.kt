import kotlin.math.abs

/**
 * Return 0 if two Float are close more than epsilon
 */
fun are_close(x: Float, y: Float, epsilon: Float = 1.0E-5F): Boolean {
    return (abs(x-y) <epsilon)
}


/**
 * return an Orthonormal base from a Vector (it will be axis-z)
 */
fun onbFromZ(normal:Normal):Triple<Vector,Vector,Vector>{
    val sign:Float=Math.copySign(1f,normal.z)
    val a:Float=-1/(normal.z+sign)
    val b:Float= normal.x*normal.y*a

    val e1 = Vector(1.0f + sign*normal.x *normal.x * a, sign * b, -sign * normal.x)
    val e2 = Vector(b, sign + normal.y * normal.y * a, -normal.y)

    return Triple(e1,e2,Vector(normal.x,normal.y,normal.z))
}

fun onbFromZ(normal:Vector):Triple<Vector,Vector,Vector>{
    val sign:Float=Math.copySign(1f,normal.z)
    val a:Float=-1/(normal.z+sign)
    val b:Float= normal.x*normal.y*a

    val e1 = Vector(1.0f + sign*normal.x *normal.x * a, sign * b, -sign * normal.x)
    val e2 = Vector(b, sign + normal.y * normal.y * a, -normal.y)

    return Triple(e1,e2,Vector(normal.x,normal.y,normal.z))
}