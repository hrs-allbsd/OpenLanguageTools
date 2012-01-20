#!/bin/sh
echo Creating segmenter for $1
mkdir -p ${1}_segmenter/org/jvnet/olt/filters/${1}_segmenter/
cp en_segmenter/org/jvnet/olt/filters/en_segmenter/* ${1}_segmenter/org/jvnet/olt/filters/${1}_segmenter/
cd ${1}_segmenter/org/jvnet/olt/filters/${1}_segmenter/
for i in *
do
 cat $i | sed -e "s/Segmenter_en/Segmenter_${1}/g" | sed -e "s/org.jvnet.olt.filters.en_segmenter/org.jvnet.olt.filters.${1}_segmenter/g" > $i.new
rm -f $i
mv $i.new `echo $i | sed -e "s/Segmenter_en/Segmenter_${1}/g"`
done

