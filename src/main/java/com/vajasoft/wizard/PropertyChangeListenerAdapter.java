/*
 * PropertyChangeListenerAdapter.java
 * 
 * Created on 2.7.2007, 15:52:45
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.vajasoft.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @param K 
 * @author z705692
 */
public class PropertyChangeListenerAdapter<K extends Enum<K>> implements PropertyChangeListener {
    private GenericPropertyChangeListener<K> adaptee;

    public PropertyChangeListenerAdapter(GenericPropertyChangeListener<K> adaptee) {
        this.adaptee = adaptee;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        adaptee.propertyChange((GenericPropertyChangeEvent<K>)evt);
    }

    GenericPropertyChangeListener<K> getAdaptee() {
        return adaptee;
    }
}
