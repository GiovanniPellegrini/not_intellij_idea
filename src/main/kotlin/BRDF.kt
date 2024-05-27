import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.abs


abstract class BRDF(open val p:Pigment) {
    abstract fun eval(normal: Normal,inDir: Vector,outDir: Vector, uv: Vec2d): Color
    abstract fun scatterRay(pcg: PCG, incomingDir: Vector, interactionPoint: Point, normal: Normal, depth: Int): Ray
}

class DiffusionBRDF(override val p: Pigment, val reflectance:Float=1f): BRDF(p){
    override fun eval(normal: Normal, inDir: Vector, outDir: Vector, uv: Vec2d): Color {
        return p.getColor(uv) * (reflectance/ PI.toFloat())
    }

    override fun scatterRay(pcg: PCG, incomingDir: Vector, interactionPoint: Point, normal: Normal, depth: Int): Ray {
        val base = onbFromZ(normal)
        val cosThetaSq = pcg.randomFloat()
        val cosTheta = kotlin.math.sqrt(cosThetaSq)
        val sinTheta = kotlin.math.sqrt(1f-cosThetaSq)
        val phi = 2f * PI.toFloat() * pcg.randomFloat()

        return Ray(
            origin = interactionPoint,
            dir = base.first * sinTheta * kotlin.math.cos(phi) + base.second * sinTheta * kotlin.math.sin(phi) +
                    base.third * cosTheta,
            tMin = 1e-3f,
            tMax = Float.POSITIVE_INFINITY,
            depth = depth
        )
    }
}

class SpecularBRDF(override val p: Pigment, val thresholdAngleRad: Float = PI.toFloat()/1800f): BRDF(p){
    override fun eval(normal: Normal, inDir: Vector, outDir: Vector, uv: Vec2d): Color {
        val thetaIn = acos(normal.normalizedDot(inDir))
        val thetaOut = acos(normal.normalizedDot(outDir))

        return if(abs(thetaIn - thetaOut) < this.thresholdAngleRad) {
            this.p.getColor(uv)
        }else Color()
    }

    override fun scatterRay(pcg: PCG, incomingDir: Vector, interactionPoint: Point, normal: Normal, depth: Int): Ray {
        val rayDir = Vector(incomingDir.x, incomingDir.y, incomingDir.z)
        rayDir.normalize()
        normal.toVector().normalize()
        val dotProd = normal * rayDir

        return Ray(
            origin = interactionPoint,
            dir = rayDir - normal.toVector() * 2f * dotProd,
            tMin = 1e-5f,
            tMax = Float.POSITIVE_INFINITY,
            depth = depth
        )
    }
}