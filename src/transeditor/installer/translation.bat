@echo off

set OTECLASSPATH=TransEditor.jar;i18n;%UserProfile%\.xliffeditor;classes\dom4j-161.jar;classes\fuzzytm.jar;classes\swing-layout-1.0.1.jar;classes\xerces2.jar;classes\XliffBackConverter.jar;classes\xmlParserAPIs.jar

java -Xmx512M -classpath "%OTECLASSPATH%" -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser org.jvnet.olt.editor.translation.TransEditor

