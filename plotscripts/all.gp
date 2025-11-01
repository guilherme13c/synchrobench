set terminal qt
set title "Throughput vs Threads"
set label "Fixed list size of 1k and update rate of 10%" at graph 0.5, graph 0.8 center
set xlabel "Threads"
set ylabel "Throughput (ops/s)"
set logscale y
set grid
set key left top
set style data linespoints
plot "datfiles/all.dat" using 1:2 title "Coarse-grained" lw 2 lc 1, \
     "datfiles/all.dat" using 1:3 title "Hand-over-hand" lw 2 lc 2, \
     "datfiles/all.dat" using 1:4 title "Lazy" lw 2 lc 3
