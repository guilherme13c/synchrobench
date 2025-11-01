set terminal qt
set title "Throughput vs Threads (Coarse-grained)"
set label "Fixed list size of 100" at graph 0.5, graph 0.92 center
set xlabel "Threads"
set ylabel "Throughput (ops/sec)"
set logscale y
set grid
set key left top
set style data linespoints
plot "datfiles/fixed-list-size-coarse.dat" using 1:2 title "0%" lw 2 lc 1, \
     "datfiles/fixed-list-size-coarse.dat" using 1:3 title "10%" lw 2 lc 2, \
     "datfiles/fixed-list-size-coarse.dat" using 1:4 title "100%" lw 2 lc 3