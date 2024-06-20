/**
 * Material class: stores the material properties of an object
 *
 * @param brdf: Bidirectional Reflectance Distribution Function
 * @param emittedRad: emitted radiance of the object
 *@constructor default: brdf and emittedRad are black uniform pigments
 */
data class Material(val brdf: BRDF = DiffusionBRDF(UniformPigment()), val emittedRad: Pigment = UniformPigment()) {}
