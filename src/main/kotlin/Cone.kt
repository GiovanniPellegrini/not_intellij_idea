import kotlin.math.pow

/**
 * Cone shape with a circumference with radius r base centered at (0,0,0) and vertex at (0,0,height) is developed
 */

class Cone(val transformation: Transformation, override val material:Material=Material(), val radius:Float, val height:Float):Shape{
    override fun pointInternal(point: Point): Boolean {
        val invPoint=transformation.inverse()*point
        if(invPoint.x.pow(2)+invPoint.y.pow(2)<=radius.pow(2) && radius.pow(2)-(point.z-height).pow(2)<=0) return true
        else return false
    }

    override fun rayIntersection(ray: Ray): HitRecord? {
        TODO("Not yet implemented")
    }
    override fun rayIntersectionList(ray: Ray): List<HitRecord>? {
        TODO("Not yet implemented")
    }


}