import kotlin.math.PI

abstract class BRDF(open val p:Pigment) {
    abstract fun eval(normal: Normal,inDir: Vector,outDir: Vector, uv: Vec2d): Color
    abstract fun scatterRay(pcg: PCG, incomingDir: Vector, interactionPoint: Point, normal: Normal, depth: Int): Ray
}

class DiffusionBRDF(override val p: Pigment, val reflectance:Float=1f): BRDF(p){
    override fun eval(normal: Normal, inDir: Vector, outDir: Vector, uv: Vec2d): Color {
        return p.getColor(uv) * (reflectance/ PI.toFloat())
    }

    override fun scatterRay(pcg: PCG, incomingDir: Vector, interactionPoint: Point, normal: Normal, depth: Int): Ray {
        TODO("Not yet implemented")
    }
}