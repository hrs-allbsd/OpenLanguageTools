#!/bin/sh
LC_ALL=en_US.UTF-8
export LC_ALL
LANG=en_US.UTF-8
export LANG

JAXB_LIBS=../../../utilities/jars/jwsdp-1.5/jaxb/lib/jaxb-api.jar:../../../utilities/jars/jwsdp-1.5/jaxb/lib/jaxb-impl.jar:../../../utilities/jars/jwsdp-1.5/jaxb/lib/jaxb-libs.jar

JAXP_LIBS=../../../utilities/jars/jwsdp-1.5/jaxp/lib/jaxp-api.jar:../../../utilities/jars/jwsdp-1.5/jaxp/lib/endorsed/xercesImpl.jar:../../../utilities/jars/jwsdp-1.5/jaxp/lib/endorsed/dom.jar:../../../utilities/jars/jwsdp-1.5/jaxp/lib/endorsed/sax.jar:../../../utilities/jars/jwsdp-1.5/jaxp/lib/endorsed/xalan.jar

JWSDP_COMMON=../../..//utilities/jars/jwsdp-1.5/jwsdp-shared/lib/jax-qname.jar:../../..//utilities/jars/jwsdp-1.5/jwsdp-shared/lib/namespace.jar:../../..//utilities/jars/jwsdp-1.5/jwsdp-shared/lib/relaxngDatatype.jar:../../..//utilities/jars/jwsdp-1.5/jwsdp-shared/lib/xsdlib.jar


CLASSPATH=../../../build/filters.jar:../../../build/tt-common.jar:$JAXB_LIBS:$JAXP_LIBS:$JWSDP_COMMON
export CLASSPATH
xDEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=58000"


$JAVA_HOME/bin/java -Xmx128m org.jvnet.olt.filters.xliffsubsegment.XliffSubSegmenter $@

