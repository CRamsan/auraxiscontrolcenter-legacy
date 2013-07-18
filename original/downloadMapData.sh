#!/bin/bash
for i in {1..5}
do
	for j in {0..50}
	do
		for k in {0..50}
		do
			echo Downloading amerish"$i"_"$j"_"$k".png
			curl -s https://players.planetside2.com/images/map/amerish/"$i"_"$j"_"$k".jpg > amerish"$i"_"$j"_"$k".png
			echo Downloading  esamir"$i"_"$j"_"$k".png 
			curl -s https://players.planetside2.com/images/map/esamir/"$i"_"$j"_"$k".jpg > esamir"$i"_"$j"_"$k".png
			echo Downloading indar"$i"_"$j"_"$k".png
			curl -s https://players.planetside2.com/images/map/indar/"$i"_"$j"_"$k".jpg > indar"$i"_"$j"_"$k".png
			
			if [[ ! -s amerish"$i"_"$j"_"$k".png && ! -s esamir"$i"_"$j"_"$k".png && ! -s indar"$i"_"$j"_"$k".png ]]
			then
				echo amerish"$i"_"$j"_"$k".png empty
				echo esamir"$i"_"$j"_"$k".png empty
				echo indar"$i"_"$j"_"$k".png empty
				break
			fi			
		done
	done
done


