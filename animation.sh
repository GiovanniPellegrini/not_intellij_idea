for angle in $(seq 0 359); do
    angleNNN=$(printf "%03d" $angle)
    gradle run --args="demo $angle /Users/lorenzoesposito/Desktop/università/raytracing/not_intellij_idea/provarot/img$angleNNN.png"
done

ffmpeg -r 25 -f image2 -s 640x480 -i /Users/lorenzoesposito/Desktop/università/raytracing/not_intellij_idea/provarot/img%03d.png \
    -vcodec libx264 -pix_fmt yuv420p \
    spheres-perspective.mp4
