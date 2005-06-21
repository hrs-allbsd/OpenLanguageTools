#!/bin/sh

CLASSPATH=EditorSetup.jar

export CLASSPATH

java -classpath ${CLASSPATH} com.sun.tmci.editor.setup.OfflineEditorSetup
