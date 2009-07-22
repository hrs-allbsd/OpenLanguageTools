#!/bin/sh

ote_base=`dirname $0`
CLASSPATH="$ote_base/TransEditor.jar:i18n:$HOME/.xliffeditor"

for j in $ote_base/classes/*.jar ; do
	CLASSPATH="$CLASSPATH:$j"
done

java -Xmx512M -classpath ${CLASSPATH} -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser org.jvnet.olt.editor.translation.TransEditor
