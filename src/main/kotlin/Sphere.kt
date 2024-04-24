import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

class Sphere(val transformation: Transformation):Shape(transformation){

    override fun rayIntersaction(ray: Ray): HitRecord? {
        val invRay=ray.transformation(this.transformation.inverse())

        val origin=invRay.origin.toVec()
        val a=invRay.dir.sqNorm()
        val b=origin.sqNorm()
        val delta= (origin*invRay.dir)*(origin*invRay.dir)-a*(b-1)

        if(delta<=0) return null

        val deltasqr= sqrt(delta)/4
        val t1=(-origin*invRay.dir-deltasqr)/a
        val t2=(-origin*invRay.dir+deltasqr)/a

        val firstHitT:Float


        if(invRay.tMin<t1 && t1<invRay.tMax) firstHitT=t1
        else if (invRay.tMin<t1 && t1<invRay.tMax) firstHitT=t2
        else return null

        TODO("to be implemented")
    }

    fun sphereNormal(point: Point,rayDir:Vector):Normal{
        val normal=Normal(point.x,point.y,point.z)
        return if (point.toVec()*rayDir<0) normal
        else -normal
    }
    fun spherePointtoUV(point: Point):Vec2d{
        var u:Float=atan2(point.y,point.x)/(2* PI.toFloat())
        if (u<0) u=u+1
        return Vec2d(u, acos(point.z)/ PI.toFloat() )
    }
}