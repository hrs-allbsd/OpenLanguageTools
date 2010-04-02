/*
* CDDL HEADER START
*
* The contents of this file are subject to the terms of the
* Common Development and Distribution License (the "License").
* You may not use this file except in compliance with the License.
*
* You can obtain a copy of the license at LICENSE.txt
* or http://www.opensource.org/licenses/cddl1.php.
* See the License for the specific language governing permissions
* and limitations under the License.
*
* When distributing Covered Code, include this CDDL HEADER in each
* file and include the License file at LICENSE.txt.
* If applicable, add the following below this CDDL HEADER, with the
* fields enclosed by brackets "[]" replaced with your own identifying
* information: Portions Copyright [yyyy] [name of copyright owner]
*
* CDDL HEADER END
*/
/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.editor.util;

import java.util.*;
import java.util.logging.Logger;


/**
   This class is designed to encapsulate all the information needed about
   changes that can occur from one string to the next.
   <br><br>
   (it probably could be improved in terms of performance - it's written
    quite simply at the moment, using StringTokenizer and Collections
everywhere)
<br><br>
<b>Note :</b> All positional indexes are in terms of the number of words
in the sentence.<br><br>

The default string separators are " \t\b\f\n><". Note that I'm counting
whitespaces as words, since these can also differ between old and new sentences.
If you want to segment a sentence in the same way as this class does it,
use the following :<br>
<blockquote><code>StringTokenizer st = new StringTokenizer (mystring," \t\b\f\n><", true);
<code><br><br>
The default token separators can be modified using get/set methods, but note that
you should use a WordOp object of a special type (WordOp.EMPTY) to use this safely.
</blockquote>


@see java.util.StringTokenizer
@author Tim Foster

*/
public class WordOp implements Comparable {
    private static final Logger logger = Logger.getLogger(WordOp.class.getName());
    private static int[] oldPosToCharArr;
    private static int[] newPosToCharArr;

    // stuff removed from classify changes - now static variables.
    private static List changes = new ArrayList();
    private static List oldStrList = new ArrayList();
    private static List newStrList = new ArrayList();
    private static List oldWordList = new ArrayList();
    private static List newWordList = new ArrayList();

    /**
       Used to mark a WordOp that is a change from one string to another
    */
    public static final int CHANGE = 0;

    /**
       Used to mark a WordOp that is a deletion of a word
    */
    public static final int DELETE = 1;

    /**
       Used to mark a WordOp that is an insertion of a word
    */
    public static final int INSERT = 2;

    /**
       Used to mark a WordOp that is a movement of a word from one position to another
    */
    public static final int MOVE = 3;

    /**
       Used to mark a WordOp that can only be used for string differencing
    */
    public static final int EMPTY = 4;

    /**
       An array that contains strings that describe each type. For example
       <code>"WordOp.typeDesc[WordOp.CHANGE]"</code> will return "change"
    */
    public static final String[] typeDesc = { "change", "delete", "insert", "move", "empty", };
    private String oldword;
    private String newword;
    private String tokens = " \t\b\f\n><";
    private int oldpos;
    private int newpos;
    private int type;

    /**
    This constructor is designed only for EMPTY operations. That is, it creates a WordOp object
    that is intended to configure the classifyChanges() method to allow for different token
    separators - to allow the user to specify what token separators they want to use dynamically
    */
    public WordOp(String tokens, int type) {
        if (type != WordOp.EMPTY) {
            System.err.println("ERROR ! : you can only use WordOp.EMPTY for string differencing operations");
        }

        this.type = type;
        this.tokens = tokens;
    }

    /**
       This constructor is designed for CHANGE or MOVE operations (since it has 2 String parameters)

       @param word1 The "old" word
       @param word2 The "new" word
       @param type  The type of operation this WordOp describes (use one of the static int's also
       declared in this class)
       @param oldpos The old position of this word
       @param newpos The new position of this word

    */
    public WordOp(String word1, String word2, int type, int oldpos, int newpos) {
        this.oldpos = oldpos;
        this.newpos = newpos;

        this.oldword = word1;
        this.newword = word2;

        this.type = type;

        switch (type) {
        case CHANGE:
            break;

        case DELETE:
            break;

        case INSERT:
            break;

        case MOVE:
            break;

        default:
            logger.finer("Type is " + type);
            logger.warning("WARNING ! Don't know how to create WordOp of this type");

            //TODO throw an Exception ???
        }
    }

    /**
       This constructor is designed for INSERT or DELETE operations (Since it has one String parameter)

       @param word The word
       @param type  The type of operation this WordOp describes (use one of the static int's also
       declared in this class : INSERT or DELETE)
       @param pos The position of this action

    */
    public WordOp(String word, int type, int pos) {
        this.type = type;

        switch (type) {
        case DELETE:
            this.oldword = word;
            this.newword = "";

            // it being a delete, there is only 1 position to worry about
            this.oldpos = pos;
            this.newpos = pos;

            break;

        case INSERT:

            // likewise for insert, we really only care about one position
            this.newword = word;
            this.oldword = "";
            this.newpos = pos;
            this.oldpos = pos;

            break;

        default:
            logger.finer("Type is " + type);
            logger.warning("WARNING : Don't know how to create WordOp of this type");

            //TODO throw an Exception ???
        }
    }

    /**
       A convenience method implemented to allow printing of WordOp objects
    */
    public String toString() {
        boolean dontprint = false;
        String out = "Type : " + typeDesc[type] + ":";

        if (this.getOldWord().equals("not used") || this.getNewWord().equals("not used")) {
            out = out + "*";
        }

        switch (type) {
        case DELETE:
            out = out + " \"" + oldword + "\" deleted from " + oldpos;

            break;

        case INSERT:
            out = out + " \"" + newword + "\" inserted at " + newpos;

            break;

        case CHANGE:

            if (!oldword.equals(newword)) {
                out = out + " \"" + oldword + "\" at " + oldpos + " changed to \"" + newword + "\" at " + newpos;
            } else {
                out = out + " \"" + oldword + "\" !changed to \"" + newword + "\" at  " + oldpos + "," + newpos;
            }

            break;

        case MOVE:
            out = out + " \"" + oldword + "\" moved from " + oldpos + " to " + newpos;

            break;

        default:
            System.err.println("WARNING : Don't know how to print this WordOp object - it's invalid");
        }

        //if (dontprint)
        //   out="";
        return out;
    }

    /**
       This returns the "old word" from a WordOp object. An "old word" is the
       word that has been either deleted, changed from or moved from. Use this
       method when your type is one of DELETE, CHANGE or MOVE. <br><br>

       It will return null if you try and call this method on a WordOp of type
       INSERT.

       @return The old word from this WordOp

    */
    public String getOldWord() {
        return this.oldword;
    }

    /** This returns the "new word" from a WordOp object. A "new word" is the
    word that has been either inserted, changed to or moved to. In the case
    of a move, the old word will be the same as the new word. Use this method
    when your type is one of INSERT, CHANGE or MOVE.<br><br>

    It will return null if you try to call this method on a WordOp of type
    DELETE.

    @return The new word from this WordOp
    */
    public String getNewWord() {
        return this.newword;
    }

    /** This method allows you to query a WordOp object to determine what type it is.
    The types are one of the static int variables INSERT DELETE CHANGE or MOVE allowing
    the user to do a statement like :<br><br>
    <pre>

          switch(wordop.getType()){
             case WordOp.INSERT:
            // do insert stuff
            break;
         case WordOp.DELETE:
            // do delete stuff
          etc.
              }
          </pre>

      @return an integer defining what type of WordOp this is.

    */
    public int getType() {
        return this.type;
    }

    /**
       This returns the position in the sentence of the old word.
       @return An integer index (indexing characters !) determining the
                position of the old word. The index starts at 0.

    */
    public int getOldPos() {
        return this.oldpos;
    }

    /**
       This returns the position in the sentence of the new word.

       @return An integer index (indexing characters !) determining the
                position of the new word. The index starts at 0.

    */
    public int getNewPos() {
        return this.newpos;
    }

    // these two methods setting new and old position are for when we convert from
    // word-based indexes to character-based indexes...
    private void setNewPos(int pos) {
        this.newpos = pos;
    }

    private void setOldPos(int pos) {
        this.oldpos = pos;
    }

    /**

       This method takes allows the user to set the token separators that are used by the
       classifyChanges method. Note that this operation is only applicable if the WordOp
       being operated on is of type WordOp.EMPTY.

       @param tokens A string containing the token separators
       @return True if the operation was successful, false otherwise.

    */
    public boolean setTokenSeparators(String tokens) {
        boolean out = false;

        switch (this.type) {
        case EMPTY:
            this.tokens = tokens;
            out = true;

            break;

        default:
            System.err.println("WARNING : tried to set token separators on a WordOp \n" + "that was not of type WordOp.EMPTY");
        }

        return out;
    }

    /**

        This method takes allows the user to get the token separators that are used by the
        classifyChanges method. Note that this operation is only applicable if the WordOp
        being operated on is of type WordOp.EMPTY.

        @return A string containing the current token separators.

     */
    public String getTokenSeparators() {
        String out = "ERROR - cannot get tokens";

        switch (this.type) {
        case EMPTY:
            out = this.tokens;

            break;

        default:
            System.err.println("WARNING : tried to get token separators from a WordOp \n" + "that was not of type WordOp.EMPTY");
        }

        return out;
    }

    /**

       This method takes 2 strings and computes the differences between them. The WordOp objects
       that are returned now contain character-positions.
       @return A List of WordOp objects that describe the differences between the two strings.
       @param oldstring The old version of the string.
       @param newstring The new version of the string.

    */
    public List classifyChanges(String oldstring, String newstring) {
        changes.clear();
        oldStrList.clear();
        newStrList.clear();
        oldWordList.clear();
        newWordList.clear();

        StringTokenizer st = new StringTokenizer(oldstring, this.tokens, true);

        for (int i = 0; st.hasMoreElements(); i++) {
            String str = (String)st.nextElement();
            oldStrList.add(str);
        }

        st = new StringTokenizer(newstring, this.tokens, true);

        for (int i = 0; st.hasMoreElements(); i++) {
            String str = (String)st.nextElement();
            newStrList.add(str);
        }

        // Now, walk through both lists and check for < and > tokens...
        // if i find any, then i add these into the next token, so that
        // the 6 tokens
        // '<', 'font', '=', '"foo"', '>'
        // appear as one
        // '< font = "foo" >'
        // I should really make this into a private method, since the
        // same code appears pasted just below as well...
        // --------------------- code to include tags in differences -------------------
        boolean intag = false;
        String tag = "";

        for (int i = 0; i < newStrList.size(); i++) {
            String str = (String)newStrList.get(i);

            if (str.equals("<") && !intag) {
                newStrList.remove(i);
                i--;
                intag = true;
                tag = tag + str;
            } else if (str.equals(">") && intag) {
                tag = tag + str;

                newStrList.remove(i);

                //                logger.finer("new StrList tag.toLowerCase()"+tag.toLowerCase());
                newStrList.add(i, tag.toLowerCase());
                intag = false;
                tag = "";
            } else if (intag) {
                newStrList.remove(i);
                i--;
                tag = tag + str;
            }
        }

        intag = false;
        tag = "";

        for (int i = 0; i < oldStrList.size(); i++) {
            String str = (String)oldStrList.get(i);

            if (str.equals("<") && !intag) {
                oldStrList.remove(i);
                i--;
                intag = true;
                tag = tag + str;
            } else if (str.equals(">") && intag) {
                tag = tag + str;
                oldStrList.remove(i);

                //                logger.finer("Old StrList tag.toLowerCase()"+tag.toLowerCase());
                oldStrList.add(i, tag.toLowerCase());
                intag = false;
                tag = "";
            } else if (intag) {
                oldStrList.remove(i);
                i--;
                tag = tag + str;
            }
        }

        /*        logger.finer("...........................start content of oldstrlist...........................");
                for(int i=0;i<oldStrList.size();i++)
                {
                  logger.finer((String)oldStrList.get(i));
                }
                logger.finer("...........................end content of oldstrlist...........................");
                logger.finer();
                logger.finer("...........................start content of newstrlist...........................");
                for(int i=0;i<newStrList.size();i++)
                {
                  logger.finer((String)newStrList.get(i));
                }
                logger.finer("...........................end content of newstrlist...........................");
        */
        // --------------------- Now build up token --> position array  -------------------
        oldPosToCharArr = new int[oldStrList.size()];

        int thispos = 0;

        for (int i = 0; i < oldStrList.size(); i++) {
            String str = (String)oldStrList.get(i);
            oldPosToCharArr[i] = thispos;
            thispos = thispos + str.length();
        }

        newPosToCharArr = new int[newStrList.size()];
        thispos = 0;

        for (int i = 0; i < newStrList.size(); i++) {
            String str = (String)newStrList.get(i);
            newPosToCharArr[i] = thispos;
            thispos = thispos + str.length();
        }

        // build up handy WordLists (useful in the future)
        for (int i = 0; i < oldStrList.size(); i++) {
            Word word = new Word((String)oldStrList.get(i), i);
            oldWordList.add(word);
        }

        for (int i = 0; i < newStrList.size(); i++) {
            Word word = new Word((String)newStrList.get(i), i);
            newWordList.add(word);
        }

        // setup and initialise two arrays that indicate if this word
        // has been dealt with or not ...
        int[] newstrArray = new int[newStrList.size()];
        int[] oldstrArray = new int[oldStrList.size()];

        for (int i = 0; i < oldstrArray.length; i++)
            oldstrArray[i] = 0;

        for (int i = 0; i < newstrArray.length; i++)
            newstrArray[i] = 0;

        List circList = getCircularElementList(oldStrList, newStrList);

        String key = "";
        List list;
        Iterator it;

        Word word = new Word("-1", -1);

        // Now let's find out which words are not in the circular "set" in the old string
        it = oldWordList.iterator();

        while (it.hasNext()) {
            word = (Word)it.next();

            String str = word.getWord();

            //            logger.finer("str = "+str);
            if (circList.contains(str)) {
                int i = newStrList.indexOf(str);

                if (word.getPos() != i) {
                    //Create a new move object
                    //logger.finer ("Element "+str+" has moved from "+word.getPos() +" to " + i);
                    WordOp op = new WordOp(str, str, WordOp.MOVE, word.getPos(), i);
                    changes.add(op);
                }

                oldstrArray[word.getPos()] = 1;
                newstrArray[i] = 1;
                newStrList.remove(i);
                newWordList.remove(i);

                newStrList.add(i, "null word");
                newWordList.add(i, new Word("null word", i));

                circList.remove(str);
            } else {
                //logger.finer ("Element "+str+" has been deleted from old string at " +word.getPos());
                WordOp op = new WordOp(str, WordOp.DELETE, word.getPos());
                changes.add(op);
                oldstrArray[word.getPos()] = 1;
            }
        }

        it = newWordList.iterator();

        while (it.hasNext()) {
            word = (Word)it.next();

            String str = word.getWord();

            if (circList.contains(str)) {
                System.err.println("WARNING ! : " + str + " should not be here - error during differencing");
            } else {
                int i = newStrList.indexOf(str);

                if (newstrArray[i] == 0) {
                    //logger.finer ("Element "+ str+" has been added to new String at " +word.getPos());
                    //Check to see if there has been a delete operation at this position
                    WordOp op = getDeleteAt(changes, word.getPos());

                    if (op == null) {
                        WordOp insop = new WordOp(str, WordOp.INSERT, word.getPos());
                        changes.add(insop);
                    } else {
                        WordOp changeop = new WordOp(op.getOldWord(), str, WordOp.CHANGE, word.getPos(), word.getPos());
                        changes.add(changeop);
                    }
                }
            }
        }

        // This isn't very good.
        changes = removeRedundantMoves(changes);

        /*        logger.finer("changes.size() = "+changes.size());
                for(int i=0;i<changes.size();i++)
                {
                  logger.finer("changes["+i+"]="+((WordOp)changes.get(i)).toString());
                }*/
        changes = convertWordPosToCharPos(changes);

        return changes;
    }

    private static WordOp getDeleteAt(List changes, int pos) {
        Iterator it = changes.iterator();
        int i = 0;

        while (it.hasNext()) {
            WordOp op = (WordOp)it.next();

            if ((op.getOldPos() == pos) && (op.getType() == WordOp.DELETE)) {
                changes.remove(i);

                WordOp newop = op;

                return newop;
            }

            i++;
        }

        return null;
    }

    // A set of mappings is "circular" if every element in list one, exists in list two
    // I can't use "Set" directly, since Sets can only contain unique elements,
    // so I have to do some playing around with Sets and Maps to achieve the same
    // effects...
    private static List getCircularElementList(List oldwords, List newwords) {
        Set oldSet = new HashSet();
        Set newSet = new HashSet();

        // I'm using these string-integer maps to keep track of the number of
        // occurrences of each element in the source and target
        // String --> Integer, (here "a" appears twice, everything else appears once)
        // a --> 2
        // b --> 1
        // c --> 1
        Map oldElementMap = new HashMap();
        Map newElementMap = new HashMap();
        Integer el = new Integer(0);
        int val = 0;

        Iterator it = oldwords.iterator();

        while (it.hasNext()) {
            String str = (String)it.next();

            if (!oldSet.add(str)) {
                // Unable to add that element - guess it already exists
                el = (Integer)oldElementMap.get(str);
                val = el.intValue();
                val++;
                oldElementMap.put(str, new Integer(val));
            } else { // add this element to our map
                oldElementMap.put(str, new Integer(1));
            }
        }

        it = newwords.iterator();

        while (it.hasNext()) {
            String str = (String)it.next();

            // now do the same for the target element
            if (!newSet.add(str)) {
                // Unable to add that element - it already exists in the set
                el = (Integer)newElementMap.get(str);
                val = el.intValue();
                val++;
                newElementMap.put(str, new Integer(val));

                //		    if (!newSet.add(str+val)){logger.finer ("ERROR !"+str+val+
                //								  "during string differencing");}
            } else { // add this element to our map
                newElementMap.put(str, new Integer(1));
            }
        }

        // Now I have 2 sets containing all the words in the set.
        oldSet.retainAll(newSet);

        // the contents of oldSet is the intersection of the two sets
        // - ie, it's all the circular elements : What we're looking for !
        // Now create a list containing minimum # occurrences of these circular elements
        List circList = new ArrayList();
        it = oldSet.iterator();

        while (it.hasNext()) {
            String str = (String)it.next();
            Integer sourceint = (Integer)oldElementMap.get(str);

            if (sourceint == null) {
                sourceint = new Integer(65535);
            }

            Integer targetint = (Integer)newElementMap.get(str);

            if (targetint == null) {
                targetint = new Integer(65535);
            }

            int i = Math.min(sourceint.intValue(), targetint.intValue());

            while ((i != 0) && (i != 65535)) {
                circList.add(str);
                i--;
            }
        }

        return circList;
    }

    // This method is designed to delete any moves that are really just
    // the result of an insert or a delete. Eg.
    // This is text
    // This is big text
    //
    // would report "text" as having moved - which isn't true.
    // I'm not checking for movement of strings within text at the moment : eg.
    // this is Solaris
    // Solaris this is
    // - at the moment, that string would be reported as having all strings moving, not just
    // "Solaris"
    // The edit-distance algorithm at http://www.csse.monash.edu.au/~lloyd/tildeAlgDS/Dynamic/Edit.html
    // might give me this (but is it worth it ???)
    // WARNING !! THIS METHOD IS REMOVING *ALL* MOVES NOW !!
    private static List removeRedundantMoves(List changes) {
        // first of all, put the collections in order
        //Collections.sort(changes);

        /*logger.finer ("----------------");
        Iterator it = changes.iterator();
        while (it.hasNext()){
            WordOp op = (WordOp)it.next();
            logger.finer(op);
        }
        logger.finer ("+++++++++++++++++");
        */
        for (int i = 0; i < changes.size(); i++) {
            WordOp op = (WordOp)changes.get(i);

            //logger.finer ("Op is " + op);
            switch (op.getType()) {
            case INSERT:

                /*//String str = op.getNewWord();
                movedistance ++;
                logger.finer ("Increasing move distance to " + movedistance);

                break;
                */
                boolean deletespace = false;

                if (op.getNewWord().equals(" ")) {
                    deletespace = true;

                    int pos = op.getOldPos();

                    // check through the old string for spaces @end
                    for (int thispos = op.getNewPos();
                            ((thispos < newStrList.size()) && deletespace); thispos++) {
                        String str = (String)newStrList.get(thispos);

                        if (str.equals(" ")) {
                            deletespace = true;
                        } else {
                            deletespace = false;
                        }
                    }

                    if (!deletespace) {
                        changes.remove(i);
                        i--;
                    }
                }

                break;

            case DELETE:

                // We only want to show spaces deleted from the end of strings
                deletespace = false;

                if (op.getOldWord().equals(" ")) {
                    deletespace = true;

                    int pos = op.getOldPos();

                    // check through the old string for spaces @end
                    for (int thispos = op.getOldPos();
                            ((thispos < oldStrList.size()) && deletespace); thispos++) {
                        String str = (String)oldStrList.get(thispos);

                        if (str.equals(" ")) {
                            deletespace = true;
                        } else {
                            deletespace = false;
                        }
                    }

                    if (!deletespace) {
                        changes.remove(i);
                        i--;
                    }
                }

                //movedistance --;
                break;

            case MOVE:

                /*int oldpos = op.getOldPos();
                int newpos = op.getNewPos();
                int diff = newpos - oldpos;
                logger.finer ("Movedistance is "+movedistance+" ?= "+diff);
                if (movedistance == diff){
                    logger.finer ("We can remove " + op);
                    changes.remove(i);
                    i--;
                    }*/
                changes.remove(i);
                i--;

                break;
            }
        }

        /*logger.finer ("----------------");
        it = changes.iterator();
        while (it.hasNext()){
            WordOp op = (WordOp)it.next();
            logger.finer(op);
        }
        logger.finer ("+++++++++++++++++");*/
        return changes;
    }

    private static List convertWordPosToCharPos(List changes) {
        int oldpos = 0;
        int newpos = 0;
        List newchanges = new ArrayList();
        Iterator it = changes.iterator();

        while (it.hasNext()) {
            WordOp op = (WordOp)it.next();

            switch (op.getType()) {
            case INSERT:
                newpos = op.getNewPos();
                op.setNewPos(newPosToCharArr[newpos]);
                newchanges.add(op);

                break;

            case DELETE:
                oldpos = op.getOldPos();
                op.setOldPos(oldPosToCharArr[oldpos]);
                newchanges.add(op);

                break;

            case MOVE:
            case CHANGE:
                newpos = op.getNewPos();
                oldpos = op.getOldPos();
                op.setOldPos(oldPosToCharArr[oldpos]);
                op.setNewPos(newPosToCharArr[newpos]);
                newchanges.add(op);

                break;
            }
        }

        return newchanges;
    }

    /**
       A simple test case : takes 2 strings from the commandline and runs
       classifyChanges() on them
    */
    static void main(String[] args) {
        //TODO add a unit  test and remove main
        if (args.length != 2) {
            System.out.println("Usage : WordOp String1 String2");
            System.exit(1);
        } else {
            System.out.println(args[0]);
            System.out.println(args[1]);

            WordOp stringdifferencer = new WordOp(" \t\b\f\n><", WordOp.EMPTY);
            List changes = stringdifferencer.classifyChanges(args[0], args[1]);

            Iterator it = changes.iterator();

            while (it.hasNext()) {
                WordOp op = (WordOp)it.next();
                System.out.println(op);
            }
        }
    }

    // compareTo() method for WordOps - only use this when sorting the list of
    // WordOps. For inserts, I'm interested in the new position, for everything
    // else, I'm interested in the old position
    public int compareTo(Object o) {
        WordOp op = (WordOp)o;

        switch (op.getType()) {
        case INSERT:

            if (this.getNewPos() > op.getNewPos()) {
                return 1;
            } else if (this.getNewPos() < op.getNewPos()) {
                return -1;
            } else { // positions are the same, since this is an insert, it gets precedence

                return 1;
            }

        default:

            if (this.getOldPos() > op.getOldPos()) {
                return 1;
            } else if (this.getOldPos() < op.getOldPos()) {
                return -1;
            } else { // positions are the same, check for deletes

                if (op.getType() == DELETE) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }
}
