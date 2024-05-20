for angle in $(seq 0 90); do
    angleNNN=$(printf "%03d" $angle)
    gradle run --args="tdemo $angle /Users/lorenzoesposito/Desktop/università/raytracing/not_intellij_idea/provaMesh/img$angleNNN.png"
done

ffmpeg -r 25 -f image2 -s 640x480 -i /Users/lorenzoesposito/Desktop/università/raytracing/not_intellij_idea/provaMesh/img%03d.png \
    -vcodec libx264 -pix_fmt yuv420p \
    prism-perspective.mp4