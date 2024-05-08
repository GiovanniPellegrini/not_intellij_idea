for angle in $(seq 0 359); do
    angleNNN=$(printf "%03d" $angle)
    gradle run --args="demo $angle img$angleNNN.png"
done

ffmpeg -r 25 -f image2 -s 640x480 -i img%03d.png \
    -vcodec libx264 -pix_fmt yuv420p \
    spheres-perspective.mp4
