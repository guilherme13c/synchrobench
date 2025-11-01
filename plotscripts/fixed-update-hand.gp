set terminal qt
set title "Throughput vs Threads (Hand-over-hand)"
set label "Fixed 10% update rate" at graph 0.5, graph 0.92 center
set xlabel "Threads"
set ylabel "Throughput (ops/sec)"
set logscale y
set grid
set key left top
set style data linespoints
plot "datfiles/fixed-update-hand.dat" using 1:2 title "100" lw 2 lc rgb "red", \
     "datfiles/fixed-update-hand.dat" using 1:3 title "1k" lw 2 lc rgb "blue", \
     "datfiles/fixed-update-hand.dat" using 1:4 title "10k" lw 2 lc rgb "green"
