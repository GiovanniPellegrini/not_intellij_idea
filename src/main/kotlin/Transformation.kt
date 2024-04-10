data class Transformation(var matrix: HomMatrix, var invmatrix:HomMatrix) {
    /**
     * Null constructor: matrix and inverse are identity
     */
    constructor() : this(HomMatrix(),HomMatrix())


}

