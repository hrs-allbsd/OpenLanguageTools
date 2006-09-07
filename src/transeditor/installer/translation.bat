SET SUNTRANS_HOME="$INSTALL_PATH"

CD %SUNTRANS_HOME%

java -Xmx256M  -classpath TransEditor.jar;classes\fuzzytm.jar;classes\xerces2.jar;classes\xmlParserAPIs.jar;classes\XliffBackConverter.jar -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser -Djava.util.logging.config.file=logging.properties -Djava.library.path=%SUNTRANS_HOME%\spellchecker\lib -Deditor_home=%SUNTRANS_HOME% org.jvnet.olt.editor.translation.TransEditor
