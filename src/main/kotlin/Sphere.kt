import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

class Sphere(val transformation: Transformation): Shape{

    //i don't know if defining a translation with 0 vector has sense...
    constructor(): this(Translation(Vector(0f,0f,0f)))

    override fun rayIntersection(ray: Ray): HitRecord? {
        val invRay=ray.transformation(this.transformation.inverse())

        val origin=invRay.origin.toVec()
        val a=invRay.dir.sqNorm()
        val b=origin.sqNorm()
        val delta= (origin*invRay.dir)*(origin*invRay.dir)-a*(b-1)

        if(delta<=0) return null

        val deltaSqr= sqrt(delta)/4
        val t1=(-origin*invRay.dir-deltaSqr)/a
        val t2=(-origin*invRay.dir+deltaSqr)/a


        val firstHitT:Float = if(invRay.tMin<t1 && t1<invRay.tMax) t1
        else if (invRay.tMin<t2 && t2<invRay.tMax) {
            t2
        } else return null

        val hitPoint = invRay.at(firstHitT)
        return HitRecord(worldPoint=this.transformation * hitPoint,
                         normal=this.transformation * sphereNormal(hitPoint, ray.dir),
                         surfacePoint= spherePointToUV(hitPoint),
                         t=firstHitT,
                         ray=ray)
    }

    fun sphereNormal(point: Point, rayDir:Vector): Normal{
        val normal = Normal(point.x,point.y,point.z)
        return if (point.toVec()*rayDir<0f) normal
        else -normal
    }
    fun spherePointToUV(point: Point): Vec2d{
        var u:Float=atan2(point.y,point.x)/(2 * PI.toFloat())
        if (u<0) u += 1
        return Vec2d(u, acos(point.z)/ PI.toFloat() )
    }
}