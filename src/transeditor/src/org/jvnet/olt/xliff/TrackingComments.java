/*
 * Copyright  2005 Sun Microsystems, Inc.
 * All rights reserved Use is subject to license terms.
 *
 */
package org.jvnet.olt.xliff;


/**
 * <p>Title: A Class To Tracking Comments For Segment Or The XLIFF File</p>
 * @author Charles Liu
 */
import java.util.*;


public class TrackingComments {
    private HashMap commentsMap;

    /***********************************************************************
     *
     * Sub Class of Comments
     *
     **********************************************************************/
    public class Comments {
        String comment = null;
        boolean modified = false;

        public Comments() {
        }

        public Comments(String strInput) {
            comment = strInput;
        }

        public Comments(String strInput, boolean bInput) {
            comment = strInput;
            modified = bInput;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String strInput) {
            comment = strInput;
        }

        public void setModified(boolean bInput) {
            modified = bInput;
        }

        public boolean isModified() {
            return modified;
        }
    }

    public TrackingComments() {
        commentsMap = new HashMap();
    }

    /***********************************************************************
     *
     * Methods to handle comments
     *
     **********************************************************************/
    public void addComment(String key, String theComment) {
        commentsMap.put(key, new Comments(theComment));
    }

    public void setComment(String key, String theComment) {
        Comments comm = (Comments)commentsMap.get(key);

        if (comm == null) {
            commentsMap.put(key, new Comments(theComment, true));
        } else {
            comm.setComment(theComment);
            comm.setModified(true);
        }
    }

    public boolean isCommentModified(String key) {
        Comments comm = (Comments)commentsMap.get(key);

        if (comm == null) {
            return false;
        } else {
            return comm.isModified();
        }
    }

    public void setCommentModified(String key, boolean bInput) {
        Comments comm = (Comments)commentsMap.get(key);

        if (comm == null) {
            return;
        }

        comm.setModified(bInput);
    }

    public String getComment(String key) {
        Comments comm = (Comments)commentsMap.get(key);

        if (comm == null) {
            return null;
        } else {
            return comm.getComment();
        }
    }

    public int hasCommented(String key) {
        Comments comm = (Comments)commentsMap.get(key);

        if (comm == null) {
            return 0;
        } else {
            if (comm.getComment() == null) {
                return 0;
            } else {
                return 1;
            }
        }
    }

    public Map getCommentsMap() {
        return commentsMap;
    }
}
