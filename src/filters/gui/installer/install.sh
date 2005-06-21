#!/bin/sh


if [ -d "$JAVA_HOME" ] ; then
	JAVA="$JAVA_HOME/bin/java"
else	
	JAVA=`which java`
fi

echo "Using java in: $JAVA"
	
$JAVA -jar xliff_filters_v@VERSION_FLAT@_unix.jar
