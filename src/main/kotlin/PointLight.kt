/**
 * A point light source:
 * @param position: the position of the light source in the scene
 * @param color: the color of the light source
 * @param linearRadius: if not zero, is used to compute the solid
 * angle subtended by the light at a given distance `d` through the formula `(r / d)²
 */

data class PointLight(val position: Point, val color: Color, val linearRadius: Float=0f)