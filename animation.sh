mkdir -p demo_frames

for angle in $(seq 0 359); do
    angleNNN=$(printf "%03d" "$angle")
    ./gradlew run --args="render -i demo.txt  -p demo_frames/img$angleNNN -a flatrender --declare-float angle=$angle"
done

ffmpeg -r 25 -f image2 -s 720x720 -i demo_frames/img%03d.png \
    -vcodec libx264 -pix_fmt yuv420p \
    demo-animation.mp4
