SET OLT_HOME="$INSTALL_PATH"

CD %OLT_HOME

java -Xmx256M  -classpath classes\filters.jar;classes\tt-common.jar;classes\dtdparser121.jar org.jvnet.olt.filters.gui.XliffFilterGUI $INSTALL_PATH\resources
