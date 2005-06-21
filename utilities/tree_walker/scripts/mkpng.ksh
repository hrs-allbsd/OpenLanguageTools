#!/bin/ksh

# DIR=$1
export BATIK_PATH=/home/jc73554/svg_stuff/batik-1.1.1

for file in xml_files/*.xml
do
  # Strip directory
  dir_strip=${file##*/}    

  # Strip extension
  ext_strip=${dir_strip%.*}

  # You might want to stick in some error checking here, along
  # the lines of "if ext_strip = dir_strip, no extension means panic"

  echo $file
  echo $ext_strip

  # Do stuff here
  /home/jc73554/scripts/treeview xml_files/${ext_strip}.xml svg_files/${ext_strip}.svg
  java -mx256M -jar ${BATIK_PATH}/batik-rasterizer.jar  svg_files/${ext_strip}.svg -d png_files/${ext_strip}.png

done
