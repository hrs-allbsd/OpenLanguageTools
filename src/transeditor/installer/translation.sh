#!/bin/sh

#To set the required memory amount uncomment the line appropriate
#line below.

#default is 256M:
MEMORY=256
#MEMORY=512

ED_INSTALL_PATH=$INSTALL_PATH
ED_JAVA_HOME=$JAVA_HOME
LOGGING=-Djava.util.logging.config.file=logging.properties
xDEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=58000"


SUNTRANS_HOME="${ED_INSTALL_PATH}"
if [ ! -d "${ED_INSTALL_PATH}" ] ; then
	SUNTRANS_HOME=`dirname $0`;
fi

export SUNTRANS_HOME

cd "${SUNTRANS_HOME}"

#build the classpath
CLASSPATH="TransEditor.jar:i18n"
for j in classes/*.jar ; do
	CLASSPATH="$CLASSPATH:$j"				  
done

export CLASSPATH

LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:${SUNTRANS_HOME}/spellchecker/lib
export LD_LIBRARY_PATH

if [ -x "$ED_JAVA_HOME/bin/java" ] ; then
	ED_JAVA=${ED_JAVA_HOME}/bin/java
else
	ED_JAVA=`which java`	
fi

if [ -z "${ED_JAVA}" ] ; then 
	echo "java does not seem to be on your system path. Please set JAVA_HOME to point to instllation of java"
	exit 0
fi


echo "Using java: ${ED_JAVA}"
`${ED_JAVA} -version`
echo "Installation direcotry: ${SUNTRANS_HOME}"
echo "Classpath: ${CLASSPATH}"

		
"${ED_JAVA}" -Xmx${MEMORY}M ${DEBUG} ${LOGGING} -classpath ${CLASSPATH} -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser -Djava.library.path="${SUNTRANS_HOME}/spellchecker/lib" -Deditor_home="${SUNTRANS_HOME}" org.jvnet.olt.editor.translation.TransEditor
