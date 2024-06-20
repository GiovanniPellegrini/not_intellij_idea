import kotlin.math.max

/**
 * Renderer abstract class: defines the render method used in the scene
 *
 * @param world: World object that contains all the shapes
 * @param backgroundColor: Color object that represents the background color of the scene
 *
 */
abstract class Renderer(val world: World, val backgroundColor: Color = Color()) {
    abstract fun render(ray: Ray): Color
}

/**
 * OnOffRenderer Class: Derived from Renderer. Renders the scene with a white color if the ray intersects an object, otherwise it renders the background color
 */
class OnOffRenderer(world: World, backgroundColor: Color = Color()) :
    Renderer(world = world, backgroundColor = backgroundColor) {
    override fun render(ray: Ray): Color {
        return if (world.rayIntersection(ray) != null) Color(255f, 255f, 255f) else backgroundColor
    }
}

/**
 * FlatRenderer Class: Derived from Renderer. Renders the scene with the color of the material of the object
 */
class FlatRenderer(world: World, backgroundColor: Color = Color()) :
    Renderer(world = world, backgroundColor = backgroundColor) {
    override fun render(ray: Ray): Color {
        val intersection = world.rayIntersection(ray) ?: return backgroundColor
        val mat = intersection.shape.material
        return mat.brdf.p.getColor(intersection.surfacePoint) + mat.emittedRad.getColor(intersection.surfacePoint)
    }
}

/**
 * PathTracer Class: Derived from Renderer. Renders the scene using a path tracer algorithm
 *
 * @param maxDepth: represents the maximum number of reflections of the ray
 * @param russianRouletteLimit: the maximum depth of the ray to apply the Russian Roulette algorithm
 * @param pcg: PCG random number generator
 * @param numberOfRays: number of rays to be fired for each reflection
 *
 */
class PathTracer(
    world: World,
    backgroundColor: Color = Color(),
    val maxDepth: Int = 5,
    val russianRouletteLimit: Int = 3,
    val pcg: PCG = PCG(),
    val numberOfRays: Int = 15
) : Renderer(world, backgroundColor) {
    override fun render(ray: Ray): Color {
        if (ray.depth > maxDepth) return Color()

        val hit = world.rayIntersection(ray) ?: return backgroundColor
        val hitMaterial = hit.shape.material
        var hitColor = hitMaterial.brdf.p.getColor(hit.surfacePoint)
        val emittedRadiance = hitMaterial.emittedRad.getColor(hit.surfacePoint)

        val hitColorLum = maxOf(hitColor.r, hitColor.g, hitColor.b)
        //Russian roulette kicks in when ray.depth is greater than or equal to russianRouletteLimit
        if (ray.depth >= russianRouletteLimit) {
            val q = maxOf(0.05f, 1 - hitColorLum)
            if (pcg.randomFloat() > q) hitColor *= 1 / (1 - q)
            else return emittedRadiance
        }

        var cum = Color()
        if (hitColorLum > 0f) {
            for (i in 0 until numberOfRays) {
                val newray =
                    hitMaterial.brdf.scatterRay(pcg, hit.ray.dir, hit.worldPoint, hit.normal, depth = ray.depth + 1)
                //recursive call to render
                val newRadiance = render(newray)
                cum += newRadiance * hitColor
            }
        }
        return emittedRadiance + cum * (1f / numberOfRays.toFloat())
    }
}