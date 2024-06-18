import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.abs

/**
 * BRDF abstract class: Bidirectional Reflectance Distribution Function. It is a function that describes how light behaves with a Shape.
 *
 * @param p: Pigment, the pigment of object depending on the BRDF
 */
abstract class BRDF(open val p: Pigment) {
    /**
     * Returns the color of the object depending on the BRDF
     */
    abstract fun eval(normal: Normal, inDir: Vector, outDir: Vector, uv: Vec2d): Color

    /**
     * Returns the ray after the scattering with the surface depending on the BRDF
     */
    abstract fun scatterRay(pcg: PCG, incomingDir: Vector, interactionPoint: Point, normal: Normal, depth: Int): Ray
}

/**
 * DiffusionBRDF class: Derived from BRDF.  It describes the behavior of an ideal diffuse surface.
 *
 * @param p: Pigment, the pigment of object depending on the BRDF
 * @param reflectance: Float, the reflectance of the object
 */
class DiffusionBRDF(override val p: Pigment, val reflectance: Float = 1f) : BRDF(p) {
    override fun eval(normal: Normal, inDir: Vector, outDir: Vector, uv: Vec2d): Color {
        return p.getColor(uv) * (reflectance / PI.toFloat())
    }

    /**
     * Returns a scattered ray with a random direction in the hemisphere
     */
    override fun scatterRay(pcg: PCG, incomingDir: Vector, interactionPoint: Point, normal: Normal, depth: Int): Ray {
        val base = onbFromZ(normal)
        val cosThetaSq = pcg.randomFloat()
        val cosTheta = kotlin.math.sqrt(cosThetaSq)
        val sinTheta = kotlin.math.sqrt(1f - cosThetaSq)
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

/**
 * SpecularBRDF class: Derived from BRDF. It describes the behavior of an ideal specular surface.
 *
 * @param p: Pigment, the pigment of object depending on the BRDF
 * @param thresholdAngleRad: Float, the threshold angle in radians
 */
class SpecularBRDF(override val p: Pigment, val thresholdAngleRad: Float = PI.toFloat() / 1800f) : BRDF(p) {
    override fun eval(normal: Normal, inDir: Vector, outDir: Vector, uv: Vec2d): Color {
        val thetaIn = acos(normal.toVector() * inDir)
        val thetaOut = acos(normal.toVector() * outDir)

        return if (abs(thetaIn - thetaOut) < this.thresholdAngleRad) {
            this.p.getColor(uv)
        } else Color()
    }

    /**
     * Returns a scattered ray using the reflection formula
     */
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