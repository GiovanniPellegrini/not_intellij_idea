/**
 * Material class
 * this class is used to store the material properties of an object:
 * - BRDF (Bidirectional Reflectance Distribution Function)
 * - Pigment
 */
data class Material(val brdf: BRDF = DiffusionBRDF(UniformPigment()), val emittedRad : Pigment = UniformPigment()) {
}
