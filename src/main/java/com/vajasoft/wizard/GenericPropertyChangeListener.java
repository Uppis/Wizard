/*
 * GenericPropertyChangeListener.java
 * 
 * Created on 2.7.2007, 15:35:06
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.vajasoft.wizard;

/**
 *
 * @param K 
 * @author z705692
 */
public interface GenericPropertyChangeListener<K extends Enum<K>> {
    void propertyChange(GenericPropertyChangeEvent<K> evt);
}
