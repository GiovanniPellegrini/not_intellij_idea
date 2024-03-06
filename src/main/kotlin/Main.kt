import kotlin.math.abs

fun main() {
    println("Hello world")
    var image = HdrImage(4, 4)
    println(image.getPixel(1,1))
}

fun are_close(x: Double, y: Double, epsilon: Double = 1e-5): Boolean {
    return (abs(x-y)<epsilon)
}