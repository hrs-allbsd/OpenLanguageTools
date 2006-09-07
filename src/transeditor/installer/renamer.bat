SET SUNTRANS_HOME="$INSTALL_PATH"

CD %SUNTRANS_HOME%

java -Xmx256M  -classpath TransEditor.jar org.jvnet.olt.editor.renamer.Renamer
