#!/bin/sh

#To set the required memory amount uncomment the line appropriate
#line below.

#default is 256M:
MEMORY=256
#MEMORY=512


if [ -d "$INSTALL_PATH" ] ; then
	SUNTRANS_HOME="$INSTALL_PATH"
	export SUNTRANS_HOME
else
	echo "The installation directory does not seem to exist: $INSTALL_PATH"
	echo "Please re-install the application"
	exit 1	
fi

cd "$INSTALL_PATH"

#build the classpath
CLASSPATH="TransEditor.jar"
for j in classes/*.jar ; do
	CLASSPATH="$CLASSPATH:$j"				  
done
export CLASSPATH

LD_LIBRARY_PATH=${LD_LIBRARY_PATH}:${SUNTRANS_HOME}/spellchecker/lib
export LD_LIBRARY_PATH

# adding a debug option here : I've turned this off at the moment,
# but is handy if you know what you're doing and have the code
# and a java debug facility
xDEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=58000"

JAVA=`which java`	
if [ -d "$JAVA_HOME" ] ; then
	JAVA="$JAVA_HOME"/bin/java
fi

if [ -z "$JAVA" ] ; then 
	echo "java does not seem to be on your system path. Please set JAVA_HOME to point to instllation of java"
	exit 0
fi

LOGGING=-Djava.util.logging.config.file=logging.properties

echo "Using java: $JAVA"
echo "Installation direcotry: $INSTALL_PATH"
echo "Classpath: $CLASSPATH"

		
"$JAVA" -Xmx${MEMORY}M ${DEBUG} ${LOGGING} -classpath ${CLASSPATH} -Dorg.xml.sax.driver=org.apache.xerces.parsers.SAXParser -Djava.library.path="${SUNTRANS_HOME}/spellchecker/lib" -Deditor_home="${SUNTRANS_HOME}" org.jvnet.olt.editor.translation.TransEditor
