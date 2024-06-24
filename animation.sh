mkdir -p demo_frames

for angle in $(seq 0 359); do
    angleNNN=$(printf "%03d" "$angle")
    gradle run --args="demo $angle demo_frames/img$angleNNN.pfm demo_frames/img$angleNNN.png perspective"
done

ffmpeg -r 25 -f image2 -s 720x720 -i demo_frames/img%03d.png \
    -vcodec libx264 -pix_fmt yuv420p \
    demo-perspective.mp4
