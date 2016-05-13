/*
 * GenericPropertyChangeEvent.java
 * 
 * Created on 2.7.2007, 15:31:10
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.vajasoft.wizard;

import java.beans.PropertyChangeEvent;

/**
 *
 * @param K 
 * @author z705692
 */
public class GenericPropertyChangeEvent<K extends Enum<K>> extends PropertyChangeEvent {
    private K propertyKey;
    
    public GenericPropertyChangeEvent(Object source, K propertyKey, Object oldValue, Object newValue) {
        super(source, propertyKey.toString(), oldValue, newValue);
        this.propertyKey = propertyKey;
    }

    public K getPropertyKey() {
        return propertyKey;
    }
}
