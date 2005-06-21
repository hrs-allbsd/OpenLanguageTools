#!/bin/sh

# this process abbreviation lists and creates tokens/productions

for f in `cat $1`
do
 INLIST=`fgrep \"${f}\" *.jjt`
 if [ ! "${INLIST}" ]
 then
   echo TOKEN : \{ \<`echo ${f} | sed -e 's/\.//g'`: \"${f}\" \>\} >> tokens.txt
 fi
done
echo -----------------------------------------
for f in `cat $1`
do
 INLIST=`fgrep \"${f}\" *.jjt`
 if [ ! "${INLIST}" ]
 then
    echo      \(t=\<`echo ${f} | sed -e 's/\.//g'`\> \{s=t.image\;\}\) \| >> abbrev.txt
 fi
done

