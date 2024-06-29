Welcome to ray tracing for dummies!

This is a simple tutorial that will help you understand how to define a scene in NIJ tracer.

## Define a scene

Open your favorite text editor and copy the following lines:

    % This is a comment
    material sphere_material(
                specular(uniform(<0.5, 0.5, 0.5>)),
                uniform(<0, 0, 0>)
            )

    sphere sfera1(translation(<1, 1, 1>) * scaling(<0.1,0.1,0.1>), sphere_material)
    camera(perspective, rotation_z(30) * translation(<-4, 0, 1>), 1.0, 1.0)

Congratulations! You have just defined a scene with a sphere and a camera now you can run the NIJ tracer.

Don't worry if you don't understand the code yet, we will explain it in the next sections.

## Point, Vectors and Colors

In a lot of objects is necessary to define a point, a vector or a color. In NIJ tracer you can define them using the
following syntax:

    % Point
    (x, y, z)

    % vector
    [x, y, z]

    % color
    <r, g, b>

    ^ Wow that's easy! ^

Where x, y, z, r, g, b are floating point numbers.
If you didn't notice inline comments in NIJ tracer are defined using the '%' symbol, and block comments are defined
using the '^' symbol.

## Transformations

When you define a scene you can move stretch or rotate objects using transformations.

In NIJ tracer you can define a transformation using the following syntax:

    % Translation
    translation([x, y, z])

    % Scaling
    scaling([x, y, z])

    % Rotation
    rotation_x(angle)
    rotation_y(angle)
    rotation_z(angle)

    % Identity
    identity

    % Composition
    transformation1 * transformation2

Remember that the order of the transformations is important.

## Camera

The camera is a special object in the scene that defines the point of view of the observer.
It is mandatory to define a camera in the scene, if you don't define it in your .txt file NIJ, tracer will assign a default camera.

You can define a camera using the following syntax:

    camera(typeOfCamera, transformation, aspectRatio, distance)

Where:

- ```typeOfCamera``` can be Perspective or Orthogonal
- ```transformation``` is a transformation that defines the position of the camera,
- ```aspectRatio``` is a floating point number
- ```distance``` is the distance between the observer and the screen

## Materials

Every shape in the scene must have a material, you can define anywhere in the file a material as long as before you use
it for a shape you have defined it

    ^ if i defined a shape before the material
      sphere mysphere(translation(<1, 1, 1>) * scaling(<0.1,0.1,0.1>), material_name)
     the compiler will throw an error ^

    
    material material_name(
                typeOfBRDF(Uniform),
                Uniform(Color)
            )

BRDF is the acronym for Bidirectional Reflectance Distribution Function, it is a function that defines how light
interacts with the surface of an object.
You can choose between the following types of BRDF:

- ```Diffuse``` diffusive surface
- ```Specular``` mirror-like surface

Remember that BRDF needs a pigment to work, you can define a pigment using the following syntax:

    pigmentKeyword(Color) 

    % pigmentKeyword can be uniform, checkered and image see at the bottom of the page for more information

Did you think we were done? objects can emit light so ***another pigment*** must be added after the brdf to represent
the radiance emitted by the object.

## Shapes

In NIJ tracer you can define a lot of shapes!
In principle, the grammar is as follows:

    shape shape_name(transformation, material)

However, let us briefly illustrate all the shapes.

### Sphere and Plane

    sphere sphere_name(transformation, material)
    plane plane_name(transformation, material)

The NIJ RayTracing algorithm works by transforming rays into the reference system of the objects in the scene.

The default sphere (transformation = identity) is centered at the origin and has a radius of 1.0, the default plane is the xy plane.

### Box

Box are defined by two points, the minimum and maximum points of the box

    box box_name(minPoint, maxPoint, transformation, material)

### Triangle

    triangle triangle_name(point1, point2, point3, transformation, material)

``` point1```, ```point2``` and  ```point3``` are the vertices of the triangle.

### Triangle Mesh

triangle mesh can be defined in two ways:

- By defining the vertices and the indices of the vertices that form the triangles

        trianglemesh triangle_mesh_name(vertices, indices, transformation, material)

  for example:

        triangle_mesh triangle_mesh_name(
            (
                (2.0, 3.0, 4.0),
                (1.3, 0.3, 0.1),
                (1.3, 1.7, 9.2),
                (1.9, 1.4, 4.5)
            ),
            ((0, 1, 2), (1, 2, 3)), transformation, material
        )

- By a .obj file ([See here for more info](https://en.wikipedia.org/wiki/Wavefront_.obj_file))

        trianglemesh triangle_mesh_name("path/to/file.obj", transformation, material)

### CSG (Constructive Solid Geometry)

CSG is a technique used to create complex shapes by combining simpler shapes using boolean operations.

In NIJ tracer are defined three types of CSG operations: Union, Intersection and Difference.

to use CSG you must define two shapes and then combine them using the following syntax:

    union(shape1, shape2, material)
    intersection(shape1, shape2, material)
    difference(shape1, shape2, material)

For example:

    material new_material(
        specular(uniform(<0.5, 0.5, 0.5>)),
        uniform(<0, 0, 0>)
    )

    material sky_material(
        diffuse(uniform(<0, 0, 0>)),
        uniform(<0.7, 0.5, 1>)
    )
        
    sphere sphere1(identity,sky_material)
    plane plane1(rotation_z(30),sky_material)
        
    CSGUnion csg1(sphere1,plane1,rotation_y(30), new_material)

If you use CSG the shapes used in the operation will be ignored, only the result of the operation will be rendered.

## Pigments

Pigments are used to define the color of the object, you can define a pigment using the following syntax:

    pigmentKeyword(Color)

Where pigmentKeyword can be:
- ```uniform```: a single color

  example:
  
        uniform(<0.5, 0.5, 0.5>)
    

- ```checkered```: a checkered pattern

    example:
    
            checkered(<0.5, 0.5, 0.5>, <0.1, 0.1, 0.1>, n_steps)
- ```image```: attach an image to the object, the image must be a .pfm file

    example:
    
            image("path/to/image.png")


## Point Lights

In NIJ Tracer you can use a simpler ray tracing algorithm that uses point lights to illuminate the scene.

Every point light has its position (point) and color

    pointlight (position, color)

Remember that if you define a point light you must use the Point-light ray tracing algorithm.



## Other features

- Between definitions, you can add whitespaces and new lines as you like, the compiler will ignore them.

      plane      myplane  
    
              (identity, material)

      % this line will not throw an error

- You cant redeclare a shape a material or a float variable with the same identifier

      material material_name(
                typeOfBRDF(Uniform),
                Uniform(Color)
            )

      material material_name(
                typeOfBRDF(Uniform),
                Uniform(Color)
            ) % this will throw an error

- Using render command with `--declare-float`, if in the input.txt file there is already a variable with the same identifier, it will be overridden with the one passed through the command line, for example if you run:
      
      ./NIJ render -i input.txt -a pathtracer --declare-float a=2

  and you input.txt file looks like

      float a(32)
      
      material my_material(diffusive(uniform(<0,0,1>)), uniform(<0,0,0>)

      plane my_plane(translation([0,0,a])

  the value passed to translation vector will be a=2, so remember that command line always win.










