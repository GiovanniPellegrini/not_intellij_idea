import kotlin.math.PI

abstract class BRDF(open val p:Pigment) {
    abstract fun eval(normal:Normal,inDir:Vector,outDir:Vector,uv:Vec2d):Color
}

class DiffusionBRDF(override val p: Pigment, val refletance:Float=1f):BRDF(p){
    override fun eval(normal: Normal, inDir: Vector, outDir: Vector, uv: Vec2d): Color {
        return p.getColor(uv)*(refletance/ PI.toFloat())
    }
}