SET SUNTRANS_HOME="$INSTALL_PATH"

CD %SUNTRANS_HOME%

java -Xmx256M  -classpath TransEditor.jar;classes\fuzzytm.jar;classes\xerces2.jar;classes\xmlParserAPIs.jar;classes\XliffBackConverter.jar -Djava.util.logging.config.file=logging.properties -Djava.library.path=%SUNTRANS_HOME%\spellchecker\lib -Dseditor_home=%SUNTRANS_HOME% org.jvnet.olt.editor.translation.TransEditor
