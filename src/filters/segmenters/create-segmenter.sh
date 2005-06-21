#!/bin/sh
echo Creating segmenter for $1
mkdir -p ${1}_segmenter/com/sun/tlis/filters/${1}_segmenter/
cp en_segmenter/com/sun/tlis/filters/en_segmenter/* ${1}_segmenter/com/sun/tlis/filters/${1}_segmenter/
cd ${1}_segmenter/com/sun/tlis/filters/${1}_segmenter/
for i in *
do
 cat $i | sed -e "s/Segmenter_en/Segmenter_${1}/g" | sed -e "s/com.sun.tlis.filters.en_segmenter/com.sun.tlis.filters.${1}_segmenter/g" > $i.new
rm -f $i
mv $i.new `echo $i | sed -e "s/Segmenter_en/Segmenter_${1}/g"`
done

