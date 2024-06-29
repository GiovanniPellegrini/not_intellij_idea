NIJ function `render` creates by default a .png output from a .pfm file

It uses a tone mapping algorithm that depends on two parameters:
- `a` is a parameter that multiplies the color radiance divided by the mean luminosity of the image
- `gamma` is a luminosity factor related to the visualization on a monitor

Here are some examples of conversions of the same .pfm image with different parameters


|                                                       a = 0.15, gamma = 1                                                        |                                                        a = 0.15, gamma = 22                                                        |
|:--------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------:|
| ![First Image](https://github.com/GiovanniPellegrini/not_intellij_idea/blob/master/examples/pfmtopng/prova_a015_g1.png?raw=true) | ![Second Image](https://github.com/GiovanniPellegrini/not_intellij_idea/blob/master/examples/pfmtopng/prova_a015_g22.png?raw=true) |


|                                                        a = 0.5, gamma = 1                                                        |                                                        a = 0.50, gamma = 22                                                        |
|:--------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------:|
| ![First Image](https://github.com/GiovanniPellegrini/not_intellij_idea/blob/master/examples/pfmtopng/prova_a050_g1.png?raw=true) | ![Second Image](https://github.com/GiovanniPellegrini/not_intellij_idea/blob/master/examples/pfmtopng/prova_a050_g22.png?raw=true) |


Remember that if you are not satisfied by the image after the rendering, you can use the command `pfm2png` to set these parameters to your liking. 