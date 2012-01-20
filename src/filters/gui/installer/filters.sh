#!/bin/sh

#To set the required memory amount uncomment the line appropriate
#line below.

#default is 256
MEMORY=256
#MEMORY=512


if [ -d "$INSTALL_PATH" ] ; then
	OLT_HOME="$INSTALL_PATH"
	export OLT_HOME
else
	echo "The installation directory does not seem to exist: $INSTALL_PATH"
	echo "Please re-install the application"
	exit 1	
fi

cd "$INSTALL_PATH"

CLASSPATH="resources:"
for j in classes/*.jar ; do
	CLASSPATH="$j:${CLASSPATH}"				  
done
export CLASSPATH

# adding a debug option here : I've turned this off at the moment,
# but is handy if you know what you're doing and have the code
# and a java debug facility
xDEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=58000"

JAVA=`which java`	
if [ -d "$JAVA_HOME" ] ; then
	JAVA="$JAVA_HOME"/bin/java
fi

if [ -z "$JAVA" ] ; then 
	echo "Java does not seem to be on your system path. Please set JAVA_HOME to point to installation of java"
	exit 0
fi

echo "Using java: $JAVA"
echo "Installation directory: $INSTALL_PATH"
echo "Classpath: $CLASSPATH"

		
"$JAVA" -Xmx${MEMORY}M ${DEBUG} -classpath ${CLASSPATH} org.jvnet.olt.filters.gui.XliffFilterGUI "$INSTALL_PATH/resources/"
