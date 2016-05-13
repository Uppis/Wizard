/*
 * Wizard.java
 * 
 * Created on 8.6.2007, 12:03:27
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.vajasoft.wizard;

import java.beans.PropertyChangeListener;

/**
 *
 * @param K 
 * @author z705692
 */
public interface Wizard<K extends Enum<K>> {
    public void addRoute(WizardCard from, K byProperty, WizardCard to);
    
    public void nextPhase();
    public void previousPhase();
    public void reset();

    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void addPropertyChangeListener(K propertyKey, PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(K propertyKey, PropertyChangeListener listener);
    public Object getProperty(K key);
    public void  setProperty(K key, Object value);
}
