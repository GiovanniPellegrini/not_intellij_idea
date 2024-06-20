package compiler

/**
 * KeyWordEnum: List of all the keywords of the language

 */
enum class KeyWordEnum {
    MATERIAL,
    SHAPE,
    PLANE,
    SPHERE,
    BOX,
    CSGUNION,
    CSGDIFFERENCE,
    CSGINTERSECTION,
    DIFFUSE,
    SPECULAR,
    UNIFORM,
    CHECKERED,
    IMAGE,
    IDENTITY,
    TRANSLATION,
    ROTATION,
    ROTATION_X,
    ROTATION_Y,
    ROTATION_Z,
    SCALING,
    CAMERA,
    ORTHOGONAL,
    PERSPECTIVE,
    FLOAT,
    TRIANGLE,
    TRIANGLEMESH,
    POINTLIGHT,
    CONE
}

val KEYWORDS: Map<String, KeyWordEnum> = mapOf(
    "material" to KeyWordEnum.MATERIAL, "Material" to KeyWordEnum.MATERIAL,
    "shape" to KeyWordEnum.SHAPE, "Shape" to KeyWordEnum.SHAPE,
    "plane" to KeyWordEnum.PLANE, "Plane" to KeyWordEnum.PLANE,
    "sphere" to KeyWordEnum.SPHERE, "Sphere" to KeyWordEnum.SPHERE,
    "box" to KeyWordEnum.BOX, "Box" to KeyWordEnum.BOX,
    "csgunion" to KeyWordEnum.CSGUNION, "CSGUnion" to KeyWordEnum.CSGUNION,
    "csgdifference" to KeyWordEnum.CSGDIFFERENCE, "CSGDifference" to KeyWordEnum.CSGDIFFERENCE,
    "csgintersection" to KeyWordEnum.CSGINTERSECTION, "CSGIntersection" to KeyWordEnum.CSGINTERSECTION,
    "diffuse" to KeyWordEnum.DIFFUSE, "Diffuse" to KeyWordEnum.DIFFUSE,
    "specular" to KeyWordEnum.SPECULAR, "Specular" to KeyWordEnum.SPECULAR,
    "uniform" to KeyWordEnum.UNIFORM, "Uniform" to KeyWordEnum.UNIFORM,
    "checkered" to KeyWordEnum.CHECKERED, "Checkered" to KeyWordEnum.CHECKERED,
    "image" to KeyWordEnum.IMAGE, "Image" to KeyWordEnum.IMAGE,
    "identity" to KeyWordEnum.IDENTITY, "Identity" to KeyWordEnum.IDENTITY,
    "translation" to KeyWordEnum.TRANSLATION, "Translation" to KeyWordEnum.TRANSLATION,
    "rotation" to KeyWordEnum.ROTATION, "Rotation" to KeyWordEnum.ROTATION,
    "rotation_x" to KeyWordEnum.ROTATION_X, "Rotation_X" to KeyWordEnum.ROTATION_X,
    "rotation_y" to KeyWordEnum.ROTATION_Y, "Rotation_Y" to KeyWordEnum.ROTATION_Y,
    "rotation_z" to KeyWordEnum.ROTATION_Z, "Rotation_Z" to KeyWordEnum.ROTATION_Z,
    "scaling" to KeyWordEnum.SCALING, "Scaling" to KeyWordEnum.SCALING,
    "camera" to KeyWordEnum.CAMERA, "Camera" to KeyWordEnum.CAMERA,
    "orthogonal" to KeyWordEnum.ORTHOGONAL, "Orthogonal" to KeyWordEnum.ORTHOGONAL,
    "perspective" to KeyWordEnum.PERSPECTIVE, "Perspective" to KeyWordEnum.PERSPECTIVE,
    "float" to KeyWordEnum.FLOAT, "Float" to KeyWordEnum.FLOAT,
    "triangle" to KeyWordEnum.TRIANGLE, "Triangle" to KeyWordEnum.TRIANGLE,
    "trianglemesh" to KeyWordEnum.TRIANGLEMESH, "TriangleMesh" to KeyWordEnum.TRIANGLEMESH,
    "pointlight" to KeyWordEnum.POINTLIGHT, "PointLight" to KeyWordEnum.POINTLIGHT,
    "cone" to KeyWordEnum.CONE, "Cone" to KeyWordEnum.CONE
)