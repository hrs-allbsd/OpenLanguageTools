Open Language XLIFF Translation Editor Installation 
===================================================

Requirements
------------

Before installation of the editor, please make sure Java
environment is installed on your system and works.

You can download the Java JRE (or JDK) from sun's pages
(http://java.sun.com)

The editor requires for proper function Java JRE version 
1.4.2_03 or later or Java 5 (a.k.a Java 1.5)

For Solaris/Linux/UNIX please make sure X11 window system
is installed and works properly
	
Installation
-----------

Windows:
	click on install.bat. 
	
	Installer window appears.  Follow the instructions in installer window

Solaris/Linux/UNIX
	Open a session in X11. In command shell type:
	sh install.sh
	
	Installer window appears.  Follow the instructions in installer window
	

Troubleshooting
---------------

Please follow these steps:

Windows:
	1. Please make sure proper version of Java  JRE is installed on your system:
	Go to Start menu, click on Run...
	Type in cmd.exe (for Win2000, WinXP) or command.com (for 
	Windows 95, Windows 98, or Windows ME)
	
	java -version

	Text, similar to the text below should appear:
	
	java version "1.5.0_04"
	Java(TM) 2 Runtime Environment, Standard Edition (build 1.5.0_04-b05)
	Java HotSpot(TM) Client VM (build 1.5.0_04-b05, mixed mode, sharing)

	Please check the version of java you use is at least 1.4.2_03. If it's not
	download and install appropriate version of Java JRE on your system.

	2. If you are sure correct version of java is installed on your system,
	try to click on the transeditor_v<VERSION>_win.jar file (the <VERSION>
	string is something like 1_1 or 1_0 for editors 1.1 or 1.0
	
Solaris/Linux/UNIX:
	1. Please make sure proper version of Java JRE is installed on your system.
	Open shell window and type:

	java -version

	A text similar to the text below should appear:
	
	java version "1.4.2_07"
	Java(TM) 2 Runtime Environment, Standard Edition (build 1.4.2_07-b05)
	Java HotSpot(TM) Client VM (build 1.4.2_07-b05, mixed mode)

	Please check the version of java you use is at least 1.4.2_03. If it's not
	download and install appropriate versio of Java JRE on your system.

	2. If you are sure correct version of java is installed on your system,
	try to run the following command in your shell:
	
	java -jar transeditor_v<VERSION>_unix.jar 
	
	(the <VERSION> string is something like 1_1 or 1_0 for editors 1.1 or 1.0)
	
If your problems persist, please contact us at this e-mail address:

users@open-language-tools.java.net

The OLT XLIFF Translation Editor Team.





	
