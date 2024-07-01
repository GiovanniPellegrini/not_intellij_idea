# NOT INTELLIJ IDEA PROJECT
This is a ray tracing project for the course named "numerical computation for photorealistic image generation", held by Prof. [Tomasi](https://github.com/ziotom78) at university of Milan.

The code is written in pure Kotlin and implements some numeric methods to solve the render equation.

Main contributors are [Giovanni Pellegrini](https://github.com/GiovanniPellegrini) and [Lorenzo Esposito](https://github.com/lorenzoesposito2)

![release](https://img.shields.io/github/v/release/GiovanniPellegrini/not_intellij_idea)
![Top Language](https://img.shields.io/github/languages/top/GiovanniPellegrini/not_intellij_idea)
![GitHub](https://img.shields.io/github/license/GiovanniPellegrini/not_intellij_idea)


# Table of contents
- [NOT INTELLIJ IDEA PROJECT](#not-intellij-idea-project)
- [Requirements](#Requirements)
- [Getting started](#Getting-started)
- [Usage](#Usage)
    - [Demo](#Demo)
    - [Render](#Render)
    - [Pfm2png](#Pfm2png)
- [Gallery](#Gallery)
- [Animations](#animations)
- [License](#License)
- [Contributing](#Contributing)

# Requirements
The software uses [Kotlin](https://kotlinlang.org/docs/home.html) version v 1.9.22, [Java JDK 21](https://www.oracle.com/it/java/technologies/downloads/#java21) or higher is required.

The build is managed by [Gradle](https://gradle.org) v 8.5.

The only external library used is [Clickt](https://ajalt.github.io/clikt/), a command line interface library for Kotlin.
Make sure that in file `build.gradle.kts` the dependency is correctly set up, as follows:
```kotlin
dependencies {
    implementation("com.github.ajalt.clikt:clikt:4.3.0")
}
```

# Getting started
To use the code, clone the repository from your terminal with the following command:
```shell
git clone git@github.com:GiovanniPellegrini/not_intellij_idea.git
```

This will create a folder named `not_intellij_idea` in your current directory.

To build the project, navigate to the root folder of the project and run the following command:
```shell
./gradlew installdist
```

It will create a folder named `build` in the root directory, containing all the compiled code.

To run the code, navigate to the folder
```shell
` cd not_intellij_idea/build/install/bin` 
```

and run the following command:
```shell
./NIJ
```

This will show the help message of the program, with the all the available commands.

# Usage

After running `./NIJ` you will see the following help message with all the available commands:
```shell
Options:
  -h, --help  Show this message and exit

Commands:
  demo     Create a demo image with 10 spheres from demo txt
  render   Create a demo image with two different algorithm from a txt file
  pfm2png  Convert a PFM file to a PNG image
```

To use a NIJ command:
```shell
./NIJ <command>
```

Some flags will appear: 
```shell
Usage: nij demo [<options>]

  Create a demo image with 10 spheres from demo txt

Options:
-r, --rotation=<float>  rotation angle of the camera (default=0)
-p, --pfm=<text>        .pfm filename Output
-o, --output=<text>     Output .png filename
-s, --save=<value>      save .pfm Output
-h, --help              Show this message and exit
```
To use those flags write them after the command, for example:
```shell
./NIJ <command> -f <file_name> -a <algorithm>
```




## Demo
This is a demonstration command that uses FlatRender algorithm to create a demo image with 10 spheres from [demo.txt](https://github.com/GiovanniPellegrini/not_intellij_idea/blob/master/src/main/kotlin/examples/demo.txt) file.

If you want you can modify the file (see the [tutorial](https://github.com/GiovanniPellegrini/not_intellij_idea/blob/master/src/main/kotlin/examples/tutorial.md) on how to define scenes)

## Render
Render is the main command of the program. Starting with a scene defined in a text file, uses several algorithms to render the image.

The command has the following flags:
- `-i, --input` text input file name
- `-o, --output` output image file name
- `-w, --image-width` image width
- `-h, --image-height` image height
- `-a, --algorithm` algorithm to use

For the more intrepid, there are some further flags to use if you know the pathTracing algorithm, be careful as changing these parameters may increase the rendering time significantly.

- `-m, --maxDepth` Maximum number of reflections of the rays
- `-r, --russianRouletteLimit` Number of reflections of a ray for which the russian roulette mechanism is activated
- `-s, --raysForSide` Number of rays fired for each side of the pixel (Antialiasing)

To declare floating point variables there is the flag
- `-D, --declare-float` for example `-D a=0.5 b=1.4`
Remember that if the variable is already declared in the file, the value passed as a flag will overwrite the previous one.

## Pfm2png
Every image created by the program is saved also in an HDR format, in particular in the Portable FloatMap format (PFM).

The conversion from pfm to png ([S&M Tone-Mapping](https://books.google.it/books/about/Realistic_Ray_Tracing_Second_Edition.html?id=ywOtPMpCcY8C&redir_esc=y)) depends on several factors. A feature implemented allows these parameters to be changed. 

The command has the following flags:
- `-i, --input` .pfm input file name
- `-o, --output` output image file name
- `-a, --aValue` rescaling factor 
- `-g, --gamma` gamma correction value for the monitor

# Gallery

![First Image](https://github.com/GiovanniPellegrini/not_intellij_idea/blob/master/examples/pfmtopng/bowl.png?raw=true)


|                                                          Cornell box with antialiasing                                                           |                                                       Cornell box without antialiasing                                                       |
|:------------------------------------------------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------------------------------------------:|
| ![Image description](https://github.com/GiovanniPellegrini/not_intellij_idea/blob/master/examples/cornellbox/cornell_antialiasing2.png?raw=true) | ![Second Image](https://github.com/GiovanniPellegrini/not_intellij_idea/blob/master/examples/cornellbox/cornell_noAntialiasing.png?raw=true) |


# Animations

If you want to create an animation you can render multiple images and then merge them to create a video.

There is already a demo script called `animation.sh` from which to take inspiration.

After rendering all the images we use the open source software [ffmpeg](https://ffmpeg.org) to create the video.

# License

NIJ is licensed under the MIT License. See the [license.txt](https://github.com/GiovanniPellegrini/not_intellij_idea/blob/master/license.txt) file for details.

# Contributing
Feel free to contribute to this project:

- **Open a pull request**: If you have written code that improves the project, you can submit it as a pull request.
- **Report bugs**: If you find a bug, you can report it by creating an issue. Please provide a detailed description of the bug and include the steps necessary to reproduce it.

You can contact us on our github profiles
-[Giovanni Pellegrini](https://github.com/GiovanniPellegrini)
-[Lorenzo Esposito](https://github.com/lorenzoesposito2)











