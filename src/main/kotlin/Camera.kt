/**
 * abstract class (interface) for camera types
 */
interface Camera {
    /**
     * virtual method for fireRay
     */
    abstract fun fireRay(u:Float, v:Float):Ray
}