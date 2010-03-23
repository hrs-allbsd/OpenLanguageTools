#!/bin/sh

ote_base=`dirname $0`
CLASSPATH="$ote_base/TransEditor.jar:$ote_base/resources.jar:$ote_base/i18n:$ote_base/plugins/resources:$HOME/.xliffeditor/plugins/resources"

for j in $ote_base/classes/*.jar ; do
	CLASSPATH="$CLASSPATH:$j"
done

for j in $ote_base/plugins/*.jar ; do
	CLASSPATH="$CLASSPATH:$j"
done

for j in $HOME/.xliffeditor/plugins/*.jar ; do
	CLASSPATH="$CLASSPATH:$j"
done


java -Xmx512M -classpath ${CLASSPATH} -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser org.jvnet.olt.editor.translation.TransEditor
