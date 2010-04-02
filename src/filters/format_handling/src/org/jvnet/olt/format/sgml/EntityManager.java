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

package org.jvnet.olt.format.sgml;

import org.jvnet.olt.format.GlobalVariableManager;
import org.jvnet.olt.format.GlobalVariableManagerException;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

import java.io.Serializable;

/**
 * This class is a manager class that stores SgmlEntity object for
 * retrieval later. We only have three types of entity available :
 * PARAMETER, SYSTEM, INTERNAL, CHARACTER
 */
public class EntityManager
        implements GlobalVariableManager, Serializable {

    // only 3 types of entity available in this impl.
    public static final int PARAMETER = 0;
    public static final int SYSTEM = 1;
    public static final int INTERNAL = 2;
    public static final int CHARACTER = 3;

    public static final String TYPE_INTERNAL = "INTERNAL";
    public static final String TYPE_SYSTEM = "SYSTEM";
    public static final String TYPE_PARAMETER = "PARAMETER";
    public static final String TYPE_CHARACTER = "CHARACTER";


    private HashMap typeMap = null;

    private HashMap parameterEntityMap;
    private HashMap systemEntityMap;
    private HashMap internalEntityMap;
    private HashMap characterEntityMap;

    private String type = null;

    /**
     * Creates a new EntityManager object.
     */
    public EntityManager() {
        parameterEntityMap = new HashMap();
        systemEntityMap = new HashMap();
        internalEntityMap = new HashMap();
        characterEntityMap = new HashMap();

        typeMap = new HashMap();
        typeMap.put(TYPE_INTERNAL, new Integer(INTERNAL));
        typeMap.put(TYPE_SYSTEM, new Integer(SYSTEM));
        typeMap.put(TYPE_PARAMETER, new Integer(PARAMETER));
        typeMap.put(TYPE_CHARACTER, new Integer(CHARACTER));
    }

    /**
     * This method allows a client to determine if a variable/entity has
     * been registered with the EntityManager. We search in the order
     * PARAMETER->INTERNAL->SYSTEM->CHARACTER
     *
     * @param variable The name of the variable to look for.
     * @return Returns true if a variable with the given name has been registered with the
     */
    public boolean isVariableDefined(String variable, String type) {
        // remove the '%' and ';' from the string if they exist
        if (variable == null || "".equals(variable))
            return false;

        String flag = extractName(variable);
        Integer i = (Integer) typeMap.get(type);
        if (i == null) {
            return false;
        }
        else {
            switch (i.intValue()) {
                case PARAMETER:
                    return parameterEntityMap.containsKey(flag);
                case INTERNAL:
                    return internalEntityMap.containsKey(flag);
                case SYSTEM:
                    return systemEntityMap.containsKey(flag);
                case CHARACTER:
                    return characterEntityMap.containsKey(flag);
                default:
                    return false;
            }
        }
    }

    /**
     * This tells us if a variable is defined
     */
    public boolean isVariableDefined(String variable) {
        return
                isVariableDefined(variable, TYPE_PARAMETER) ||
                isVariableDefined(variable, TYPE_INTERNAL) ||
                isVariableDefined(variable, TYPE_SYSTEM) ||
                isVariableDefined(variable, TYPE_CHARACTER);
    }


    /**
     * This variable returns the value of the variable, unless this is
     * another registered variable. In that case it resolves the second
     * variable first, and returns the result. If it's unable to resolve
     * this entity, we return the empty string "". We try to resolve variables
     * in the order PARAMETER->INTERNAL->SYSTEM->CHARACTERS
     * <p/>
     * This is mostly here for backwards compatibility - if you know what
     * sort of variable you're looking for, then use the resolveVariable(variable,type)
     * method instead.
     *
     * @param variable The name of the variable to look for.
     * @return The value associated with the variable.
     */
    public String resolveVariable(String variable) {
        String resolvedValue = "";
        try {
            SgmlEntity entity;
            entity = resolveEntity(variable, TYPE_PARAMETER);
            if (entity == null) {
                entity = resolveEntity(variable, TYPE_INTERNAL);
            }
            if (entity == null) {
                entity = resolveEntity(variable, TYPE_SYSTEM);
            }
            if (entity == null) {
                entity = resolveEntity(variable, TYPE_CHARACTER);
            }
            if (entity == null) {
                entity = new SgmlEntity("", "", "");
            }
            resolvedValue = entity.getValue();

        }
        catch (GlobalVariableManagerException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return resolvedValue;
    }

    public String resolveVariable(String variable, String type) {
        SgmlEntity entity = null;

        try {
            entity = resolveEntity(variable, type);
        }
        catch (GlobalVariableManagerException e) {
            System.err.println(e.getMessage());
        }
        if (entity == null) {
            return "";
        }
        return entity.getValue();
    }

    /**
     * extract entity name between % and ;
     * if str is null then null is returned<br>
     * Otherwise string without '%' in the beggining and ';' at the end
     * will be returned
     *
     * @param str var name to exatract
     * @return extracetd name
     */
    String extractName(String str) {
        if (str == null)
            return null;
        if (str.length() > 2) {
            if (str.charAt(0) == '%') {
                str = str.substring(1, str.length());
            }
            if (str.charAt(str.length() - 1) == ';') {
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    private SgmlEntity resolveEntity(String variable, String type) throws GlobalVariableManagerException {
        SgmlEntity entity = null;

        if (variable == null || "".equals(variable))
            return null;


        String flag = extractName(variable);
        Integer i = (Integer) typeMap.get(type);

        if (i == null) {
            throw new GlobalVariableManagerException("Unknown variable type " + type);
        }
        int variableType = i.intValue();
        switch (variableType) {
            case PARAMETER:
                entity = (SgmlEntity) parameterEntityMap.get(flag);
                break;
            case INTERNAL:
                entity = (SgmlEntity) internalEntityMap.get(flag);
                break;
            case SYSTEM:
                entity = (SgmlEntity) systemEntityMap.get(flag);
                break;
            case CHARACTER:
                entity = (SgmlEntity) characterEntityMap.get(flag);
                break;
        }
        if (entity == null) {
            return null;
        }
        // finally, if we get another parameter entity back, recursively
        // figure out what that entity resolves to
        String tmp = extractName(entity.getValue());

        //System.out.println("currently looking at " + entity.getType()+":"+entity.getValue());
        if (TYPE_PARAMETER.equals(entity.getType()) &&
                !(tmp.equals("IGNORE") || tmp.equals("INCLUDE"))) {
            entity = resolveEntity(tmp, TYPE_PARAMETER);
        }
        return entity;
    }

    /**
     * This method allows a client to determine the type of a registered
     * variable. We look for entities in the order, PARAMETER->INTERNAL->SYSTEM->CHARACTER
     *
     * @param variable The name of the variable to look for.
     * @return A string representing the type of the variable.
     */
    public String getVariableType(String variable) {
        SgmlEntity entity = null;
        try {
            entity = resolveEntity(variable, TYPE_PARAMETER);
            if (entity == null) {
                entity = resolveEntity(variable, TYPE_INTERNAL);
            }
            if (entity == null) {
                entity = resolveEntity(variable, TYPE_SYSTEM);
            }
            if (entity == null) {
                entity = resolveEntity(variable, TYPE_CHARACTER);
            }

        }
        catch (GlobalVariableManagerException e) {
            System.out.println(e.getMessage());
        }
        if (entity == null) {
            return "";
        }

        return entity.getType();
    }

    /**
     * This method registers a variable with the EntityManager.
     *
     * @param name  The name of the variable to register.
     * @param value The value of the variable.
     * @param type  The variable's type.
     */
    public void setVariable(String name, String value, String type) throws GlobalVariableManagerException {
        if (name == null || name.length() == 0) {
            throw new GlobalVariableManagerException("The variable name cannot be a 0 length string");
        }
        Integer i = (Integer) typeMap.get(type);
        if (i == null) {
            throw new GlobalVariableManagerException("The variable type " + type + " is unknown");
        }
        SgmlEntity entity = new SgmlEntity(name, value, type);
        switch (i.intValue()) {
            case PARAMETER:
                parameterEntityMap.put(name, entity);
                break;
            case INTERNAL:
                internalEntityMap.put(name, entity);
                break;
            case SYSTEM:
                systemEntityMap.put(name, entity);
                break;
            case CHARACTER:
                characterEntityMap.put(name, entity);
                break;
        }
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("------------\n");
        
        // first print the parameter entities
        Set keys = parameterEntityMap.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            SgmlEntity ent = (SgmlEntity) parameterEntityMap.get(key);
            String value = ent.getValue();
            String type = ent.getType();
            buf.append(key + " = " + value + " (" + type + ")\n");
        }
        // next, the system entities
        keys = systemEntityMap.keySet();
        it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            SgmlEntity ent = (SgmlEntity) systemEntityMap.get(key);
            String value = ent.getValue();
            String type = ent.getType();
            buf.append(key + " = " + value + " (" + type + ")\n");
        }
        // next, the internal entities
        keys = internalEntityMap.keySet();
        it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            SgmlEntity ent = (SgmlEntity) internalEntityMap.get(key);
            String value = ent.getValue();
            String type = ent.getType();
            buf.append(key + " = " + value + " (" + type + ")\n");
        }
        // lastly, the character entities;
        keys = characterEntityMap.keySet();
        it = keys.iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            SgmlEntity ent = (SgmlEntity) characterEntityMap.get(key);
            String value = ent.getValue();
            String type = ent.getType();
            buf.append(key + " = " + value + " (" + type + ")\n");
        }
        buf.append("------------\n");

        return buf.toString();
    }

    /**
     * This allows us to specify the type of global variable manager this is
     */
    public String getManagerType() {
        return this.type;
    }

    /**
     * This allows us to get the type of global variable manager this is
     */
    public void setManagerType(String type) {
        this.type = type;
    }

}
