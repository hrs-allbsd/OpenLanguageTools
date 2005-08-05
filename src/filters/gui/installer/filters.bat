SET OLT_HOME="$INSTALL_PATH"

CD "%OLT_HOME"

java -Xmx256M  -classpath resources;classes\filters.jar;classes\tt-common.jar;classes\dtdparser121.jar;classes\xerces2.jar;classes\xmlParserAPIs.jar org.jvnet.olt.filters.gui.XliffFilterGUI "$INSTALL_PATH\resources"
