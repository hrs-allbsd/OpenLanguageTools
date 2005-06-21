/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.util;

import java.io.*;

import java.util.*;
import java.util.zip.*;


/**
 *
 * A class for handling Windows-style INI files. The file format is as
 * follows:  <dl>
 *<dd>   [subject]       - anything beginning with [ and ending with ] is a subject
 *<dd>   ;comment        - anything beginning with a ; or # is a comment
 *<dd>   variable=value  - anything of the format string=string is an assignment
 *<dd>   value;value     - ; is the seperator between values
 *<dd>   comment         - anything that doesn't match any of the above is a comment
 * </dl>
 *
 * @deprcecated Functionality has been replaced by Preferences
 *
 *
 */
public class IniFile extends Object {
    //    static java.util.ResourceBundle res0 = java.util.ResourceBundle.getBundle("com.sun.tmc.tmeditor.resourcebundles.UtilResource")/*#BundleType=List*/;
    private static Vector path = new Vector();

    static {
        String PATH = System.getProperty("java.class.path");
        String dir;
        File f;

        while ((PATH != null) && (PATH.length() != 0)) {
            int i = PATH.lastIndexOf(java.io.File.pathSeparatorChar);
            dir = PATH.substring(i + 1);

            if (i == -1) {
                PATH = null;
            } else {
                PATH = PATH.substring(0, i);
            }

            if (dir.equals(".")) {
                break;
            }

            f = new File(dir);

            if (f.exists()) {
                path.insertElementAt(f, 0);
            }
        }

        dir = System.getProperty("user.home");
        f = new File(dir);

        if (f.exists()) {
            path.insertElementAt(f, 0);
        }

        dir = System.getProperty("user.dir");
        f = new File(dir);

        if (f.exists()) {
            path.insertElementAt(f, 0);
        }

        dir = System.getProperty("toolkit.home");

        if (dir != null) {
            f = new File(dir);

            if (f.exists()) {
                path.insertElementAt(f, 0);
            }
        }
    }

    /**Actual text lines of the file stored in a vector.*/
    protected Vector lines = new Vector();

    /**A vector of all subjects*/
    protected Vector subjects = new Vector();

    /**A vector of variable name vectors grouped by subject*/
    protected Vector variables = new Vector();

    /**A vector of variable value vectors grouped by subject*/
    protected Vector values = new Vector();

    /**Name of the file*/
    protected String fileName;
    protected String jarFileName;

    /**If true, INI file will be saved every time a value is changed. Defaults to false*/
    protected boolean absultDir = false;

    /**A separator used in a subject*/
    protected char separator = '.';

    /**
     * Find a INI file using the specified name,
     * the file may locate in the curent dir or usr home or class path
     * If it is in the class path, It can not be saved.
     * @param name the name of the file
     */
    public IniFile(String name) {
        this(name, false);
    }

    /**
     * Creates an INI file object using the specified name
     * If the file is in jar then the system will save it in the current dir
     * @param name the name of the file
     * @param absultDir save file whenever a value is set
     */
    public IniFile(String baseName, boolean absultDir) {
        this.absultDir = absultDir;

        boolean d = false;

        if (absultDir) {
            d = getIniReader(null, baseName);

            if (d) {
                parseLines();

                return;
            }
        } else {
            baseName = baseName.replace('.', File.separatorChar);

            for (int i = 0; i < path.size(); i++) {
                d = getIniReader((File)path.elementAt(i), baseName);

                if (d) {
                    parseLines();

                    return;
                }
            }

            this.fileName = baseName + ".INI";
        }
    }

    /**
     * Find a INI file using the specified file,
     * @param file - the specified file
     * @param newSeparator - the new separator replacing the default '.'
     * @param absultDir - while true, save file whenever a value is changed
     */
    public IniFile(File file, char newSeparator, boolean absultDir) {
        fileName = file.getAbsolutePath();
        this.separator = newSeparator;

        if (file.exists()) {
            loadFile();
        }

        this.absultDir = absultDir;
    }

    public IniFile(String filenameinjar, String jarfile) {
        fileName = filenameinjar + ".INI";
        jarFileName = jarfile;
        loadFile();
    }

    /**
     * Find a INI file using the specified file
     * @param file - the specified file
     */
    public IniFile(File file) {
        this(file, '.', false);
    }

    private boolean getReader(File parentDir, String baseName) {
        if ((parentDir == null) || parentDir.isDirectory()) {
            File f = new File(parentDir, baseName);

            if (f.exists()) {
                try {
                    BufferedReader in = new BufferedReader(new FileReader(f));
                    String oneLine = "";

                    try {
                        String line = in.readLine();

                        while (line != null) {
                            oneLine += line;

                            int i = oneLine.length() - 1;

                            for (; (i >= 0) && (oneLine.charAt(i) == '\\'); i--)
                                ;

                            // empty loop
                            if (((oneLine.length() - i) % 2) == 1) {
                                if (oneLine.length() > 0) {
                                    lines.addElement(oneLine);
                                    oneLine = "";
                                }
                            } else {
                                oneLine = oneLine.substring(0, oneLine.length() - 1);
                            }

                            line = in.readLine();
                        }
                    } catch (IOException e) {
                        //UIAgent.show("INI File from dir failed: " + e.getMessage());
                    }

                    in.close();

                    return true;
                } catch (IOException e) {
                    //UIAgent.show("INI File from dir failed: " + e.getMessage());
                }
            }
        } else {
            try {
                ZipFile f = new ZipFile(parentDir);
                String s = baseName.replace(File.separatorChar, '/');
                ZipEntry entry = f.getEntry(s);

                if ((entry != null) && !entry.isDirectory()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(f.getInputStream(entry)));

                    try {
                        String oneLine = "";
                        String line = in.readLine();

                        while (line != null) {
                            oneLine += line;

                            int i = oneLine.length() - 1;

                            for (; (i >= 0) && (oneLine.charAt(i) == '\\'); i--)
                                ;

                            // empty loop
                            if (((oneLine.length() - i) % 2) == 1) {
                                if (oneLine.length() > 0) {
                                    lines.addElement(oneLine);
                                    oneLine = "";
                                }
                            } else {
                                oneLine = oneLine.substring(0, oneLine.length() - 1);
                            }

                            line = in.readLine();
                        }
                    } catch (IOException e) {
                        //UIAgent.show("INI File from jar failed: " + e.getMessage());
                    }

                    in.close();

                    return true;
                }
            } catch (IOException e) {
                //UIAgent.show("INI File from dir failed: " + e.getMessage());
            }
        }

        return false;
    }

    private boolean getIniReader(File parentDir, String baseName) {
        String locale = Locale.getDefault().toString();
        String s;

        while (true) {
            if (locale.length() == 0) {
                s = baseName + ".INI";
            } else {
                s = baseName + "_" + locale + ".INI";
            }

            boolean r = getReader(parentDir, s);

            if (r) {
                //Use absolute pathname for fileName
                String prefix = parentDir.getAbsolutePath();

                if (!prefix.endsWith(File.separator)) {
                    prefix += File.separator;
                }

                this.fileName = prefix + s;

                //this.fileName = s;
                return r;
            } else if (locale.length() == 0) {
                break;
            }

            int i = locale.lastIndexOf("_");

            if (i > 0) {
                locale = locale.substring(0, i);
            } else {
                locale = "";
            }
        }

        return false;
    }

    /**
     * When the INI file is updated, reload the file.
     */
    public void loadFile() {
        lines = new Vector();
        subjects = new Vector();
        variables = new Vector();
        values = new Vector();

        try {
            BufferedReader in;

            if (jarFileName == null) {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            } else {
                //             System.out.println("filename="+fileName);
                in = new BufferedReader(new InputStreamReader(getClass().getResource(fileName).openStream()));
            }

            try {
                String oneLine = "";
                String line = in.readLine();

                while (line != null) {
                    oneLine += line;

                    int i = oneLine.length() - 1;

                    for (; (i >= 0) && (oneLine.charAt(i) == '\\'); i--)
                        ;

                    // empty loop
                    if (((oneLine.length() - i) % 2) == 1) {
                        if (oneLine.length() > 0) {
                            lines.addElement(oneLine);
                            oneLine = "";
                        }
                    } else {
                        oneLine = oneLine.substring(0, oneLine.length() - 1);
                    }

                    line = in.readLine();
                }
            } catch (IOException e) {
                //UIAgent.show("INI File failed: " + e.getMessage());
            }

            parseLines();
        } catch (IOException e) {
            //UIAgent.show("INI File failed: " + e.getMessage());
        }
    }

    /**
     * Create a new INI file.
     */
    public boolean createFile() throws IOException {
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));
            out.write(";INI File supported by java: " + fileName + System.getProperty("line.separator"));

            return true;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Reads lines, filling in subjects, variables and values.
     */
    protected void parseLines() {
        String currentLine = null;
        String currentSubject = null;

        for (int i = 0; i < lines.size(); i++) {
            currentLine = ((String)lines.elementAt(i)).trim();

            if (isaSubject(currentLine)) {
                currentSubject = currentLine.substring(1, currentLine.length() - 1);

                String tmpString = currentSubject;
                currentSubject = getSubjectPrefix() + currentSubject;
                addSubject(currentSubject);
                pushSubject(tmpString);
            } else if (isanAssignment(currentLine)) {
                String assignment = currentLine;

                //addAssignment(getSubjectPrefix() + currentSubject,assignment);
                addAssignment(currentSubject, assignment);
            } else if (isEndNoteOfSubject(currentLine)) {
                popSubject(endNoteOfSubject(currentLine));
            }
        }
    }

    /**
     * Add a subject
     */
    protected boolean addSubject(String subject) {
        if (!subjects.contains(subject)) {
            subjects.addElement(subject);

            variables.addElement(new Vector());
            values.addElement(new Vector());

            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds an assignment (i.e. "variable=value") to a subject.
     */
    protected boolean addAssignment(String subject, String assignment) {
        String value;
        String variable;

        int index = assignment.indexOf("=");

        if (index == -1) {
            variable = assignment.trim();

            return addValue(subject, variable, "", false);
        } else {
            variable = assignment.substring(0, index).trim();
            value = assignment.substring(index + 1, assignment.length()).trim();

            if ((value.length() == 0) || (variable.length() == 0)) {
                return false;
            } else {
                return addValue(subject, variable, value, false);
            }
        }
    }

    /**
     * Sets a specific subject/variable combination the given value. If the subject
     * doesn't exist, create it. If the variable doesn't exist, create it. If
     * absultDir is true, save the file;
     * @param subject the subject heading (e.g. "Widget Settings")
     * @param variable the variable name (e.g. "Color")
     * @param value the value of the variable (e.g. "green")
     * @return true if successful
     */
    public boolean setValue(String subject, String variable, String value) {
        boolean result = addValue(subject, variable, value, true);

        /*
        System.out.println("&&&&&&&&&&& setValue "+" "+subject+"  "+variable+"  "+value);
        for(int i=0;i<lines.size();i++)
        System.out.println(lines.elementAt(i));
         */
        return result;
    }

    /**
     * Sets a specific subject/variable combination the given value. If the subject
     * doesn't exist, create it. If the variable doesn't exist, create it.
     * @param subject the subject heading (e.g. "Widget Settings")
     * @param variable the variable name (e.g. "Color")
     * @param value the value of the variable (e.g. "green")
     * @param addToLines add the information to the lines vector
     * @return true if successful
     */
    protected boolean addValue(String subject, String variable, String value, boolean addToLines) {
        if ((subject == null) || (subject.length() == 0)) {
            return false;
        }

        if ((variable == null) || (variable.length() == 0)) {
            addBlankSubject(subject);

            return true;

            //return false;
        }

        /*        if (!subjects.contains(subject)) {
                    subjects.addElement(subject);

                    variables.addElement(new Vector());
                    values.addElement(new Vector());
                }
         */
        int subjectIndex = subjects.indexOf(subject);

        if (subjectIndex == -1) {
            addSubject(subject);
        }

        subjectIndex = subjects.indexOf(subject);

        Vector subjectVariables = (Vector)(variables.elementAt(subjectIndex));
        Vector subjectValues = (Vector)(values.elementAt(subjectIndex));

        if (!subjectVariables.contains(variable)) {
            subjectVariables.addElement(variable);
            subjectValues.addElement(value);
        }

        int variableIndex = subjectVariables.indexOf(variable);
        subjectValues.setElementAt(value, variableIndex);

        if (addToLines) {
            setLine(subject, variable, value);
        }

        return true;
    }

    /**
     * does the line represent a subject?
     * @param line a string representing a line from an INI file
     * @return true if line is a subject
     */
    protected boolean isaSubject(String line) {
        return (line.startsWith("[") && line.endsWith("]"));
    }

    /**
     * set a line in the lines vector
     * @param subject the subject heading (e.g. "Widget Settings")
     * @param variable the variable name (e.g. "Color")
     * @param value the value of the variable (e.g. "green")
     */
    protected void setLine(String subject, String variable, String value) {
        int subjectLine = findSubjectLine(subject);

        if (subjectLine == -1) {
            char[] sep = { getSeparator() };
            StringTokenizer st = new StringTokenizer(subject, new String(sep));
            String sub = "";

            int num = st.countTokens();

            for (int i = 0; i < num; i++) {
                String token = st.nextToken();
                sub = sub + token;

                if (findSubjectLine(sub) == -1) {
                    addBlankSubject(sub);
                }

                sub = sub + (new String(sep));
            }

            //subjectLine = lines.size()-1;
            subjectLine = findSubjectLine(subject);
        }

        int endOfSubject = beforeEndOfSubject(subjectLine);
        int lineNumber = findAssignmentBetween(variable, subjectLine, endOfSubject);

        if (value == null) {
            value = "";
        } else {
            value = "=" + value;
        }

        if (lineNumber == -1) {
            lines.insertElementAt(addAssignmentInsetSpace(subject) + variable + value, endOfSubject);
        } else {
            lines.setElementAt(addAssignmentInsetSpace(subject) + variable + value, lineNumber);
        }
    }

    /**
     * find the line containing a variable within a subject
     * @param subject the subject heading (e.g. "Widget Settings")
     * @param variable the variable name (e.g. "Color")
     * @return the line number of the assignment, -1 if not found
     */
    protected int findAssignmentLine(String subject, String variable) {
        int start = findSubjectLine(subject);
        int end = beforeEndOfSubject(start);

        return findAssignmentBetween(variable, start, end);
    }

    /**
     * find the line containing a variable within a range of lines
     * @param variable the variable name (e.g. "Color")
     * @param start the start of the range (inclusive)
     * @param end the end of the range (exclusive)
     * @return the line number of the assignment, -1 if not found
     */
    protected int findAssignmentBetween(String variable, int start, int end) {
        for (int i = start; i < end; i++) {
            if (((String)lines.elementAt(i)).startsWith(variable + "=")) {
                //if (((String)lines.elementAt(i)).trim().startsWith(variable)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * insert inset spaces at the begining of a subject
     * This is reserved for ExtIniFile to override
     */
    protected String addSubjectInsetSpace(String subject) {
        return "";
    }

    /**
     * insert inset spaces at the begining of an assignment
     * This is reserved for ExtIniFile to override
     */
    protected String addAssignmentInsetSpace(String subject) {
        return "";
    }

    /**
     * add a subject start line to the end of the lines vector
     * @param subject the subject heading (e.g. "Widget Settings")
     */
    protected void addSubjectStartLine(String subject, String insets) {
        lines.addElement(insets + "[" + subject + "]");
    }

    /**
     * add a subject end line to the end of the subject
     */
    protected void addSubjectEndLine(String subject, String insets) {
        lines.addElement("");
    }

    /**
     * Only add a subject without variables and values
     */
    protected void addBlankSubject(String subject) {
        int subjectIndex = subjects.indexOf(subject);

        if (subjectIndex == -1) {
            addSubject(subject);
        }

        int subjectLine = findSubjectLine(subject);

        if (subjectLine == -1) {
            addSubjectStartLine(subject, addSubjectInsetSpace(subject));
            addSubjectEndLine(subject, addSubjectInsetSpace(subject));
        }
    }

    /**
     * find a subject line within the lines vector
     * @param subject the subject heading (e.g. "Widget Settings")
     * @return the line number of the subject, -1 if not found
     */
    protected int findSubjectLine(String subject) {
        String line;
        String formattedSubject = "[" + subject + "]";

        for (int i = 0; i < lines.size(); i++) {
            line = (String)lines.elementAt(i);

            if (formattedSubject.equals(line.trim())) {
                return i;
            }
        }

        return -1;
    }

    /**
     * find the line number which is 1 past the last assignment in a subject
     * starting at a given line
     * @param start the line number at which to start looking
     * @return the line number of the last assignment + 1
     */
    protected int endOfSubject(int start) {
        int endIndex = start + 1;

        if (start >= lines.size()) {
            return lines.size();
        }

        for (int i = start + 1; i < lines.size(); i++) {
            if (isanAssignment((String)lines.elementAt(i))) {
                endIndex = i + 1;
            }

            if (isaSubject((String)lines.elementAt(i))) {
                return endIndex;
            }
        }

        return endIndex;
    }

    /**
     * reserved for ExtIniFile to override
     */
    protected int beforeEndOfSubject(int start) {
        return endOfSubject(start);
    }

    /**
     * If the current line is an end note of a subject, e.g. "/[...]"
     * @ param line the current line
     * @ return true if it is an end note of a subject
     */
    protected boolean isEndNoteOfSubject(String line) {
        line = line.trim();

        return (line.startsWith("/") && (line.indexOf("[") != -1) && (line.endsWith("]")));
    }

    /**
     * Get the end note of a subject
     * @param line the current line
     * @return the end note of a subject
     */
    protected String endNoteOfSubject(String line) {
        if (isEndNoteOfSubject(line)) {
            return line.substring(line.indexOf("[") + 1, line.indexOf("]"));
        } else {
            return null;
        }
    }

    /**
     * does the line represent an assignment?
     * @param line a string representing a line from an INI file
     * @return true if line is an assignment
     */
    protected boolean isanAssignment(String line) {
        return (!isaSubject(line) && (!line.startsWith(";")) && (!line.startsWith("#")) && (!isEndNoteOfSubject(line)));

        //return ((line.indexOf("=")!=-1) && (!line.startsWith(";")) && (!line.startsWith("#")));
    }

    /**
     * get a copy of the lines vector
     */
    public Vector getLines() {
        return (Vector)lines.clone();
    }

    /**
     * get a vector containing all variables in a subject
     * @param subject the subject heading (e.g. "Widget Settings")
     * @return a list of variables, empty vector if subject not found
     */
    public String[] getVariables(String subject) {
        String[] v;
        int index = subjects.indexOf(subject);

        if (index != -1) {
            Vector vars = (Vector)(variables.elementAt(index));
            v = new String[vars.size()];
            vars.copyInto(v);

            return v;
        } else {
            v = new String[0];

            return v;
        }
    }

    /**
     * get an array containing all subjects
     * @return a list of subjects
     */
    public String[] getAllSubjects() {
        String[] s = new String[subjects.size()];
        subjects.copyInto(s);

        return s;
    }

    /**
     * get a method within a subject
     * @param heading the heading of the subject
     * @param subject the subject
     * @return the method
     */
    public String getMethod(String heading, String subject) {
        String result;
        String m;
        m = subject.substring(heading.length() + 1);

        if (m.indexOf(getSeparator()) != -1) {
            result = m.substring(0, m.indexOf(getSeparator()));
        } else {
            result = m;
        }

        return result;
    }

    /**
     * get methods within a subject
     * @param heading the heading of the subject
     * @return the method array
     */
    public String[] getMethods(String heading) {
        String[] results;
        String[] subjects;
        subjects = getSubjects(heading);
        results = new String[subjects.length];

        for (int i = 0; i < subjects.length; i++) {
            results[i] = getMethod(heading, subjects[i]);
        }

        return results;
    }

    /**
     * get the subjects with the same heading
     * @param heading the heading of subjects
     * @return subjects
     */
    public String[] getSubjects(String heading) {
        Vector v = new Vector();
        String[] result;

        for (int i = 0; i < subjects.size(); i++) {
            String sub = (String)subjects.elementAt(i);

            if ((sub.indexOf(heading) != -1) && (sub.length() > heading.length())) {
                v.addElement(sub);
            }
        }

        result = new String[v.size()];
        v.copyInto(result);

        return result;
    }

    /**
     * get the value of a variable within a subject
     * @param subject the subject heading (e.g. "Widget Settings")
     * @param variable the variable name (e.g. "Color")
     * @return the value of the variable (e.g. "green"), empty string if not found
     */
    public String getValue(String subject, String variable) {
        int subjectIndex = subjects.indexOf(subject);

        if (subjectIndex == -1) {
            return "";
        }

        Vector valVector = (Vector)(values.elementAt(subjectIndex));
        Vector varVector = (Vector)(variables.elementAt(subjectIndex));
        int valueIndex = varVector.indexOf(variable);

        if (valueIndex != -1) {
            return (String)(valVector.elementAt(valueIndex));
        }

        return "";
    }

    /**
     * get all the values seperated by ;
     * @param subject the subject heading
     * @param variable the variable name
     * @return the String array of the values
     */
    public String[] getValues(String subject, String variable) {
        String[] result = null;

        String values = getValue(subject, variable);
        StringTokenizer st = new StringTokenizer(values, ";");
        int count = st.countTokens();

        if (count > 0) {
            result = new String[count];

            for (int i = 0; (i < count) && st.hasMoreTokens(); i++) {
                String s = st.nextToken();

                if (!"null".equals(s)) {
                    result[i] = s.trim();
                }
            }
        }

        return result;
    }

    /**
     * delete variable within a subject
     * @param subject the subject heading (e.g. "Widget Settings")
     * @param variable the variable name (e.g. "Color")
     * @return if success, return true or return false
     */
    public void deleteValue(String subject, String variable) {
        int subjectIndex = subjects.indexOf(subject);

        if (subjectIndex == -1) {
            return;
        }

        Vector valVector = (Vector)(values.elementAt(subjectIndex));
        Vector varVector = (Vector)(variables.elementAt(subjectIndex));

        int valueIndex = varVector.indexOf(variable);

        if (valueIndex != -1) {
            valVector.removeElementAt(valueIndex);
            varVector.removeElementAt(valueIndex);

            int assignmentLine = findAssignmentLine(subject, variable);

            if (assignmentLine != -1) {
                lines.removeElementAt(assignmentLine);
            }

            if (varVector.size() == 0) {
                deleteSubject(subject);
            }

            if (absultDir) {
                try {
                    saveFile();
                } catch (IOException e) {
                } catch (FileAccessException e) {
                }
            }
        }
    }

    /**
     * delete a subject and all its variables
     * @param subject the subject heading (e.g. "Widget Settings")
     */
    public void deleteSubject(String subject) {
        int subjectIndex = subjects.indexOf(subject);

        if (subjectIndex == -1) {
            return;
        }

        values.removeElementAt(subjectIndex);
        variables.removeElementAt(subjectIndex);
        subjects.removeElementAt(subjectIndex);

        int start = findSubjectLine(subject);
        int end = endOfSubject(start);

        for (int i = start; i < end; i++) {
            lines.removeElementAt(start);
        }

        if (absultDir) {
            try {
                saveFile();
            } catch (IOException e) {
            } catch (FileAccessException e) {
            }
        }
    }

    /**
     * save the lines vector back to the INI file
     */
    public void saveFile() throws IOException, FileAccessException {
        if ((fileName != null) || (fileName.length() > 0)) {
            try {
                File file = new File(fileName);

                if (!file.exists()) {
                    createFile();
                }

                if (!file.canWrite()) {
                    throw new FileAccessException();
                }

                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName)));

                for (int i = 0; i < lines.size(); i++) {
                    out.write((String)(lines.elementAt(i)) + System.getProperty("line.separator"));
                }

                out.close();
            } catch (IOException e) {
            }
        } else {
            //            UIAgent.show(res0.getString("INI File name is null!"/*#Finished:Original="INI File name is null!"*/));
        }
    }

    /**
     * clean up
     */
    protected void finalize() {
        if (absultDir == true) {
            try {
                saveFile();
            } catch (IOException e) {
            } catch (FileAccessException ex) {
            }
        }
    }

    /**
     * set the separator in a subject
     * @param separator - separator string, usually a char
     */
    public void setSeparator(char separator) {
        this.separator = separator;
    }

    /**
     * get the separator string in a subject
     * @return string - separator
     */
    public char getSeparator() {
        return separator;
    }

    protected String getSubjectPrefix() {
        return "";
    }

    protected void pushSubject(String sub) {
    }

    protected void popSubject(String sub) {
    }
}
