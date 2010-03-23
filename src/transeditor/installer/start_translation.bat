@echo off

setlocal
set OTECLASSPATH=TransEditor.jar;i18n;%UserProfile%\.xliffeditor\plugins\resources;plugins\resources;resources.jar

for %%i in (classes\*.jar) do (
  set OTECLASSPATH=!OTECLASSPATH!;%%i
)

for %%i in (plugins\*.jar) do (
  set OTECLASSPATH=!OTECLASSPATH!;%%i
)

for %%i in (%UserProfile%\.xliffeditor\plugins\*.jar) do (
  set OTECLASSPATH=!OTECLASSPATH!;%%i
)

java -Xmx512M -classpath "%OTECLASSPATH%" -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser org.jvnet.olt.editor.translation.TransEditor
endlocal
