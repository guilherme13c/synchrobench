#!/bin/bash

algos=(
    "CoarseGrainedListBasedSet"
    "HandOverHandListBasedSet"
    "LazyLinkedListSortedSet"
)

threads=(1 4 6 8 10 12)
update_ratios=(0 10 100)
list_sizes=(100 1024 10000)

for algo in "${algos[@]}"; do
    for t in "${threads[@]}"; do
        for u in "${update_ratios[@]}"; do
            for i in "${list_sizes[@]}"; do
                r=$((i * 2))
                echo "Running $algo with $t threads, update ratio $u, list size $i, range $r"
                java -cp bin contention.benchmark.Test \
                    -b "linkedlists.lockbased.${algo}" \
                    -W 0 -d 2000 \
                    -t "$t" -u "$u" -i "$i" -r "$r" > data/${algo}-${t}-${u}-${i}.dat
            done
        done
    done
done

