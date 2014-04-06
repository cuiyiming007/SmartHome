package com.gdgl.manager;

import java.util.Vector;


public class Manger {
	
    private boolean changed = false;
    
    private Vector observers;
    
    /**
     * observers
     */
    public Manger()
    {
        observers = new Vector();
    }
    public synchronized void addObserver(UIListener o)
    {
        if (o == null)
        {
            throw new NullPointerException();
        }
        if (!observers.contains(o))
        {
            observers.addElement(o);
        }
    }
    
    public synchronized void deleteObserver(UIListener o)
    {
        observers.removeElement(o);
    }
    
    public void notifyObservers()
    {
        notifyObservers(null);
    }
    
    /**
     * notify all the changes observer
     * 
     * @param data
     *            event
     */
    public void notifyObservers(Object data)
    {
        
        synchronized (this)
        {
            
            if (!changed)
            {
                return;
            }
            clearChanged();
        }
        
        for (int i = 0; i < observers.size(); i++)
        {
            try
            {
                ((UIListener) observers.elementAt(i)).update();
            }
            catch (Exception e)
            {
            }
        }
    }
    
    /**
     * delete all the observers
     */
    public synchronized void deleteObservers()
    {
        observers.removeAllElements();
    }
    
    /**
     * set the all the observers changed
     */
    public synchronized void setChanged()
    {
        changed = true;
    }
    
    /**
     * set the all the observers not changed
     */
    protected synchronized void clearChanged()
    {
        changed = false;
    }
    
    /**
     * the
     * 
     * @return true is changed ,false not changed
     */
    public synchronized boolean hasChanged()
    {
        return changed;
    }
    
    /**
     * how many Observers which the object have
     * 
     * @return how many Observers which the object have
     */
    public synchronized int countObservers()
    {
        return observers.size();
    }

}
