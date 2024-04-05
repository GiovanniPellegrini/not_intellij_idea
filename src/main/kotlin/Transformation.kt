data class Transformation(var m: HomMatrix, var invm: HomMatrix) {
    fun isConsistent(): Boolean{
        val prod = m*invm
        return prod.isClose(HomMatrix())
    }

    fun translation(vec: Vector): Transformation {
        val matrix = HomMatrix()
        matrix[0,3] = vec.x
        matrix[1,3] = vec.y
        matrix[2,3] = vec.z

        val invMatrix = HomMatrix()
        matrix[0,3] = -vec.x
        matrix[1,3] = -vec.y
        matrix[2,3] = -vec.z

        return Transformation(matrix,invMatrix)
    }
}