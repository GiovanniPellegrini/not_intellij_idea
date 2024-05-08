class Triangle(val trans: Transformation = Transformation(),
               val a: Point = Point(0f,0f,0f),
               val b: Point = Point(0f,0f,0f),
               val c: Point = Point(0f,0f,0f)): Shape {

    fun isInside(alpha: Float, beta: Float):Boolean{
        return alpha>=0 && beta>=0 && alpha+beta<=1
    }

    fun trianglePoint(alpha: Float, beta: Float ): Point{
        return a + (b-a)*alpha + (c-a)*beta
    }

    override fun rayIntersection(ray: Ray): HitRecord? {
        return null
    }
}