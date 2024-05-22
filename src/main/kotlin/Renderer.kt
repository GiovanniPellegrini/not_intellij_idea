import kotlin.math.max

abstract class Renderer(val world: World, val backgroundColor: Color = Color()){
    abstract fun render(ray: Ray): Color
}

class OnOffRenderer(world: World, backgroundColor: Color = Color()): Renderer(world = world, backgroundColor = backgroundColor){
     override fun render(ray: Ray): Color{
        return if (world.rayIntersection(ray) != null) Color(255f,255f,255f) else backgroundColor
    }
}

class FlatRenderer(world: World, backgroundColor: Color = Color()): Renderer(world = world, backgroundColor = backgroundColor){
    override fun render(ray: Ray): Color {
        val intersection = world.rayIntersection(ray) ?: return backgroundColor
        val mat = intersection.shape.material
        return mat.brdf.p.getColor(intersection.surfacePoint) + mat.emittedRad.getColor(intersection.surfacePoint)
    }
}

class PathTracer(world: World, backgroundColor: Color=Color(), val maxdepth:Int=2, val russianRouletteLimit:Int=5, val pcg: PCG=PCG(),val numberOfRays:Int=100): Renderer(world,backgroundColor){
    override fun render(ray: Ray): Color {
        if(ray.depth>maxdepth) return Color()

        val hit= world.rayIntersection(ray) ?: return backgroundColor
        val hitMaterial=hit.shape.material
        var hitColor=hitMaterial.brdf.p.getColor(hit.surfacePoint)
        val emittedRadiance=hitMaterial.emittedRad.getColor(hit.surfacePoint)

        val hitColorLum= maxOf(hitColor.r,hitColor.g,hitColor.b)
        if(ray.depth>=russianRouletteLimit){
            val q= maxOf(0.05f,1-hitColorLum)
            if(pcg.randomFloat()>q) hitColor*=1/(1-q)
            else return emittedRadiance
        }

        var cum=Color()
        if(hitColorLum>0f){
            for(i in 0 until numberOfRays){
                val newray=hitMaterial.brdf.scatterRay(pcg,hit.ray.dir,hit.worldPoint,hit.normal, depth = ray.depth+1)
                val newRadiance=render(newray)
                cum+=newRadiance+hitColor
            }
        }
        return (cum+emittedRadiance)*(1f/numberOfRays)
    }
}