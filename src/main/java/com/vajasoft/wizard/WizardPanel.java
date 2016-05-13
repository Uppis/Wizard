/*
 * WizardImpl.java
 *
 * Created on 8.6.2007, 12:22:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.vajasoft.wizard;

import java.awt.CardLayout;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.Map.Entry;
import javax.swing.JPanel;

/**
 *
 * @param K
 * @author z705692
 */
public class WizardPanel<K extends Enum<K>> extends JPanel implements Wizard<K> {

    private CardLayout layout;
    private PropertyChangeSupport support;
    private Map<K, Object> properties = new HashMap<K, Object>();
    private Map<WizardCard, Map<K, WizardCard>> routes = new HashMap<WizardCard, Map<K, WizardCard>>();
    private Stack<WizardCard> phases = new Stack<WizardCard>();
    private WizardCard memory;

    public WizardPanel() {
        support = new PropertyChangeSupport(this);
        layout = new CardLayout();
        this.setLayout(layout);
    }

    public void addRoute(WizardCard from, K byProperty, WizardCard to) {
        checkPeer(from);
        checkPeer(to);
        Map<K, WizardCard> rMap = routes.get(from);
        if (rMap == null) {
            rMap = new LinkedHashMap<K, WizardCard>();
            routes.put(from, rMap);
        } else if (rMap.get(byProperty) != null) {
            throw new IllegalArgumentException("The wizard already contains route for <" + ((Component) from).getName() + "," + byProperty + ">");
        }
        rMap.put(byProperty, to);
    }

    public void previousPhase() {
        phases.pop().passivate();
        showCard(phases.peek());
    }

    public void nextPhase() {
        WizardCard current = phases.empty() ? null : phases.peek();
        // Check if a route definition exists
        Map<K, WizardCard> rMap = routes.get(current);
        if (rMap != null) {
            for (Entry<K, WizardCard> e : rMap.entrySet()) {
                if (getProperty(e.getKey()) != null) {
                    setPhase(e.getValue());
                    return;
                }
            }
        }
        // No route found, move to next phase in sequence
        Component[] comps = getComponents();
        int ix = 0;
        if (current != null) {
            // Find current component
            while (ix < comps.length && comps[ix] != current) {
                ix++;
            }
            ix++;   // Index of the component next to current
            if (ix >= comps.length) {
                throw new IllegalStateException("End of wizard reached");
            }
        }
        setPhase((WizardCard) comps[ix]);
    }

    public void setMemory() {
        if (!phases.empty()) {
            memory = phases.peek();
        }
    }

    public void resetMemory() {
        memory = null;
    }

    public void rewind() {
        if (!phases.empty()) {
            phases.peek().passivate();
        }
        while (!phases.empty() && (memory == null || memory != phases.peek())) {
            phases.pop().init();    // "Reconstruct"
        }
    }

    public void reset() {
        resetMemory();
        rewind();
        for (K p : properties.keySet()) {
            setProperty(p, null);   // Notify all the listeners
        }
        nextPhase();
    }

    public void add(WizardCard card) {
        if (!(card instanceof Component)) {
            throw new IllegalArgumentException("The card is not instance of Component : " + card.getClass().getSimpleName());
        }
        Component comp = (Component)card;
        super.add(comp, comp.getName());
        card.init();
    }

//    protected void addImpl(Component comp, Object constraints, int index) {
//        if (!(comp instanceof WizardCard)) {
//            throw new IllegalArgumentException("The component is not instance of WizardCard: " + comp.getName() + " of class " + comp.getClass().getSimpleName());
//        }
//        WizardCard card = (WizardCard) comp;
//        super.addImpl(comp, constraints != null ? constraints : comp.getName(), index);
//        card.init();
//    }

    private void setPhase(WizardCard toCard) {
        if (!phases.empty()) {
            phases.peek().passivate();
        }
        phases.push(toCard);
        showCard(toCard);
    }

    private void showCard(WizardCard toCard) {
        layout.show(this, ((Component) toCard).getName());
        toCard.activate();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(K propertyKey, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyKey.name(), listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(K propertyKey, PropertyChangeListener listener) {
        support.removePropertyChangeListener(propertyKey.name(), listener);
    }

    public Object getProperty(K name) {
        return properties.get(name);
    }

    public void setProperty(K name, Object value) {
        Object old = properties.get(name);
        properties.put(name, value);
        support.firePropertyChange(new GenericPropertyChangeEvent<K>(this, name, old, value));
    }

    private void checkPeer(WizardCard card) {
        if (card instanceof Component == false || ((Component) card).getParent() != this) {
            throw new IllegalArgumentException("Not a valid wizard card: " + card);
        }
    }
}
