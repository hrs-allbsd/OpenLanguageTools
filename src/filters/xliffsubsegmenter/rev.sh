#!/bin/sh
LC_ALL=en_US.UTF-8
export LC_ALL
LANG=en_US.UTF-8
export LANG
CLASSPATH=/space/timf/transtech/utilities/iAS7/share/lib/jaxrpc-api.jar:/space/timf/transtech/utilities/jars/jaxb/namespace.jar://space/timf/transtech/utilities/jars/jaxb/xsdlib.jar:/space/timf/transtech/utilities/jars/jaxb//jaxb-libs.jar:/space/timf/transtech/utilities/jars/jaxb//jaxb-impl.jar://space/timf/transtech/utilities/jars/jaxb//jaxb-api.jar:build:/space/timf/transtech/build/filters.jar:.
export CLASSPATH
xDEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=58000"


$JAVA_HOME/bin/java ${DEBUG} -Xmx128m com.sun.tt.filters.xliffsubsegment.XliffSubSegmentCollapser $@
