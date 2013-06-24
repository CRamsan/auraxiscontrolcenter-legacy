#!/bin/bash
for i in {1000..5000}
do
   curl http://census.soe.com/img/ps2/icon/$i/item > "$i.png"
done
